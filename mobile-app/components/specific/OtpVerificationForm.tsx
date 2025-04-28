import React, { useState, useRef, useEffect } from "react";
import {
  View,
  Text,
  TextInput,
  StyleSheet,
  TouchableOpacity,
  ActivityIndicator,
  Keyboard,
  Animated,
  ScrollView,
  AppState,
} from "react-native";
import { useRouter } from "expo-router";
import { Ionicons } from "@expo/vector-icons";
import {
  useSafeAreaInsets,
  SafeAreaView,
} from "react-native-safe-area-context";
import * as Clipboard from "expo-clipboard";

interface OtpVerificationFormProps {
  email: string;
  isForgotPassword?: boolean;
  nextRoute?: string;
}

const OtpVerificationForm: React.FC<OtpVerificationFormProps> = ({
  email,
  isForgotPassword = false,
  nextRoute = "/setupPassword",
}) => {
  const router = useRouter();
  const insets = useSafeAreaInsets();
  const [otp, setOtp] = useState(["", "", "", ""]);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [isResending, setIsResending] = useState(false);
  const [timer, setTimer] = useState(60);
  const [canResend, setCanResend] = useState(false);
  const appState = useRef(AppState.currentState);

  // Animation values
  const shakeAnimation = useRef(new Animated.Value(0)).current;
  const errorOpacity = useRef(new Animated.Value(0)).current;

  // References to input fields
  const inputRefs = useRef<Array<TextInput | null>>([]);

  // Start countdown timer on mount
  useEffect(() => {
    let interval: NodeJS.Timeout;

    if (timer > 0 && !canResend) {
      interval = setInterval(() => {
        setTimer((prev) => {
          if (prev <= 1) {
            setCanResend(true);
            clearInterval(interval);
            return 0;
          }
          return prev - 1;
        });
      }, 1000);
    }

    return () => {
      if (interval) clearInterval(interval);
    };
  }, [timer, canResend]);

  // Check clipboard on focus of first input
  useEffect(() => {
    const checkClipboard = async () => {
      try {
        const hasString = await Clipboard.hasStringAsync();
        if (hasString) {
          const clipboardContent = await Clipboard.getStringAsync();
          const digits = clipboardContent.replace(/\D/g, "");
          if (digits.length >= 4) {
            const newOtp = [
              digits[0] || "",
              digits[1] || "",
              digits[2] || "",
              digits[3] || "",
            ];
            setOtp(newOtp);

            // Clear any existing errors
            if (error) setError(null);
          }
        }
      } catch (err) {
        console.log("Failed to check clipboard", err);
      }
    };

    // Check clipboard when component mounts
    checkClipboard();

    // Add app state change listener to check clipboard when app comes to foreground
    const subscription = AppState.addEventListener("change", (nextAppState) => {
      if (
        appState.current.match(/inactive|background/) &&
        nextAppState === "active"
      ) {
        checkClipboard();
      }

      appState.current = nextAppState;
    });

    return () => {
      subscription.remove();
    };
  }, [error]);

  // Format the timer
  const formatTime = (seconds: number) => {
    const mins = Math.floor(seconds / 60);
    const secs = seconds % 60;
    return `${mins}:${secs < 10 ? "0" : ""}${secs}`;
  };

  // Handle input changes for OTP
  const handleOtpChange = (text: string, index: number) => {
    // Clear error when user starts typing
    if (error) setError(null);

    // Update OTP array
    const newOtp = [...otp];
    newOtp[index] = text;
    setOtp(newOtp);

    // Auto focus next input if value is entered
    if (text.length === 1 && index < 3) {
      inputRefs.current[index + 1]?.focus();
    }
  };

  // Handle key press for backspace
  const handleKeyPress = (e: any, index: number) => {
    if (e.nativeEvent.key === "Backspace" && !otp[index] && index > 0) {
      inputRefs.current[index - 1]?.focus();
    }
  };

  // Handle OTP verification
  const verifyOtp = async () => {
    // Check if OTP is complete
    if (otp.some((digit) => !digit)) {
      setError("Please enter the complete code");
      showErrorAnimation();
      return;
    }

    Keyboard.dismiss();
    setIsSubmitting(true);

    try {
      // Simulate API call
      await new Promise((resolve) => setTimeout(resolve, 1500));

      // Navigate to password setup or reset
      router.push(nextRoute as any);
    } catch (err) {
      setError("Invalid verification code. Please try again.");
      showErrorAnimation();
    } finally {
      setIsSubmitting(false);
    }
  };

  // Handle resend OTP
  const resendOtp = async () => {
    if (!canResend) return;

    setIsResending(true);
    setError(null);

    try {
      // Simulate API call
      await new Promise((resolve) => setTimeout(resolve, 1000));

      setTimer(60);
      setCanResend(false);
    } catch (err) {
      setError("Failed to resend code. Please try again.");
    } finally {
      setIsResending(false);
    }
  };

  // Go back to previous page
  const goBack = () => {
    router.back();
  };

  // Show error animation
  const showErrorAnimation = () => {
    // Fade in error message
    Animated.timing(errorOpacity, {
      toValue: 1,
      duration: 300,
      useNativeDriver: true,
    }).start();

    // Shake animation for OTP inputs
    Animated.sequence([
      Animated.timing(shakeAnimation, {
        toValue: 10,
        duration: 100,
        useNativeDriver: true,
      }),
      Animated.timing(shakeAnimation, {
        toValue: -10,
        duration: 100,
        useNativeDriver: true,
      }),
      Animated.timing(shakeAnimation, {
        toValue: 10,
        duration: 100,
        useNativeDriver: true,
      }),
      Animated.timing(shakeAnimation, {
        toValue: 0,
        duration: 100,
        useNativeDriver: true,
      }),
    ]).start();
  };

  // Calculate appropriate top padding
  const topPadding = Math.max(insets.top, 20);

  return (
    <ScrollView
      style={styles.container}
      contentContainerStyle={{
        paddingTop: isForgotPassword ? 20 : topPadding + 24,
      }}
    >
      {isForgotPassword && (
        <TouchableOpacity onPress={goBack} style={styles.backButton}>
          <Ionicons name="chevron-back" size={24} color="#868D96" />
        </TouchableOpacity>
      )}

      <View style={styles.header}>
        <Text style={styles.title}>OTP Verification</Text>
        <Text style={styles.subtitle}>
          Enter the verification code we just sent to your email{" "}
          <Text style={styles.emailText}>{email}</Text>
        </Text>
      </View>

      <Animated.View
        style={[
          styles.otpContainer,
          { transform: [{ translateX: shakeAnimation }] },
        ]}
      >
        {otp.map((digit, index) => (
          <TextInput
            key={index}
            ref={(el) => (inputRefs.current[index] = el)}
            style={[
              styles.otpInput,
              error ? styles.otpInputError : digit ? styles.otpInputFilled : {},
            ]}
            value={digit}
            onChangeText={(text) =>
              handleOtpChange(text.replace(/\D/g, ""), index)
            }
            onKeyPress={(e) => handleKeyPress(e, index)}
            keyboardType="number-pad"
            maxLength={1}
            selectTextOnFocus
            editable={!isSubmitting}
          />
        ))}
      </Animated.View>

      <Animated.View style={[styles.errorContainer, { opacity: errorOpacity }]}>
        {error && (
          <Text style={styles.errorText}>
            <Ionicons name="alert-circle" size={16} color="#DC2626" /> {error}
          </Text>
        )}
      </Animated.View>

      <View style={styles.resendContainer}>
        <Text style={styles.resendText}>You didn't receive a code?</Text>
        <TouchableOpacity
          onPress={resendOtp}
          disabled={!canResend || isResending || isSubmitting}
        >
          {isResending ? (
            <ActivityIndicator
              size="small"
              color="#00643C"
              style={styles.resendLoader}
            />
          ) : (
            <Text
              style={[
                styles.resendButtonText,
                !canResend && styles.resendButtonDisabled,
              ]}
            >
              {canResend ? "Resend" : `Resend (${timer}s)`}
            </Text>
          )}
        </TouchableOpacity>
      </View>

      <TouchableOpacity
        style={[
          styles.verifyButton,
          (isSubmitting || otp.some((digit) => !digit)) &&
            styles.verifyButtonDisabled,
        ]}
        onPress={verifyOtp}
        disabled={isSubmitting || otp.some((digit) => !digit)}
      >
        {isSubmitting ? (
          <ActivityIndicator color="white" size="small" />
        ) : (
          <Text style={styles.verifyButtonText}>Verify</Text>
        )}
      </TouchableOpacity>
    </ScrollView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    paddingHorizontal: 20,
  },
  backButton: {
    width: 40,
    height: 40,
    borderRadius: 20,
    backgroundColor: "#E6F0EC",
    justifyContent: "center",
    alignItems: "center",
    marginBottom: 32,
  },
  header: {
    marginBottom: 32,
  },
  title: {
    fontSize: 32,
    fontFamily: "Poppins-SemiBold",
    color: "#333",
    marginBottom: 8,
  },
  subtitle: {
    fontSize: 12,
    fontFamily: "Poppins-Regular",
    color: "#868D96", // meristem-grey-300
    lineHeight: 24,
  },
  emailText: {
    fontFamily: "Poppins-Medium",
    color: "#00643C", // meristem-green
  },
  otpContainer: {
    flexDirection: "row",
    justifyContent: "space-between",
    marginBottom: 24,
  },
  otpInput: {
    width: 70,
    height: 70,
    borderRadius: 12,
    borderWidth: 1,
    borderColor: "#E0E0E0",
    textAlign: "center",
    fontSize: 24,
    fontFamily: "Poppins-Medium",
    backgroundColor: "#FBFBFB",
    color: "#005030", // meristem-green-active color
  },
  otpInputFilled: {
    borderColor: "#00643C", // meristem-green
    backgroundColor: "#F0F9F3", // Light green background
  },
  otpInputError: {
    borderColor: "#DC2626", // Error red
    backgroundColor: "#FEF2F2", // Light red background
  },
  errorContainer: {
    marginBottom: 24,
  },
  errorText: {
    color: "#DC2626",
    fontSize: 14,
    fontFamily: "Poppins-Regular",
    alignItems: "center",
  },
  resendContainer: {
    flexDirection: "row",
    justifyContent: "center",
    alignItems: "center",
    marginBottom: 40,
  },
  resendText: {
    fontFamily: "Poppins-Regular",
    fontSize: 14,
    color: "#868D96", // meristem-grey-300
    marginRight: 4,
  },
  resendButtonText: {
    fontFamily: "Poppins-Medium",
    fontSize: 14,
    color: "#00643C", // meristem-green
  },
  resendButtonDisabled: {
    color: "#ACB1B7", // meristem-grey-200
  },
  resendLoader: {
    marginLeft: 8,
  },
  verifyButton: {
    backgroundColor: "#00643C", // meristem-green
    borderRadius: 50,
    paddingVertical: 16,
    alignItems: "center",
    justifyContent: "center",
    marginTop: 24,
  },
  verifyButtonDisabled: {
    backgroundColor: "#ACB1B7", // meristem-grey-200
  },
  verifyButtonText: {
    color: "white",
    fontFamily: "Poppins-Medium",
    fontSize: 16,
  },
});

export default OtpVerificationForm;
