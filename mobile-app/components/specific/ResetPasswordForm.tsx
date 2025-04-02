import { Ionicons } from "@expo/vector-icons";
import { zodResolver } from "@hookform/resolvers/zod";
import { useRouter } from "expo-router";
import React, { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import {
    ActivityIndicator,
    KeyboardAvoidingView,
    Platform,
    StyleSheet,
    Text,
    TouchableOpacity,
    View,
} from "react-native";
import Animated, {
    FadeInDown
} from "react-native-reanimated";
import { z } from "zod";
import PasswordInput from "../common/PasswordInput";
import PasswordRequirement from "../common/PasswordRequirement";

// Password validation schema
const passwordSchema = z
  .object({
    password: z.string().min(1, "Password is required"),
    confirmPassword: z.string().min(1, "Please confirm your password"),
  })
  .refine((data) => data.password === data.confirmPassword, {
    message: "Passwords don't match",
    path: ["confirmPassword"],
  });

type FormData = z.infer<typeof passwordSchema>;

const ResetPasswordForm = () => {
  const router = useRouter();
  const [isSubmitting, setIsSubmitting] = useState(false);

  // Password requirements state
  const [requirements, setRequirements] = useState({
    length: false,
    uppercase: false,
    number: false,
    special: false,
  });

  // Count number of requirements met
  const requirementsMet = Object.values(requirements).filter(Boolean).length;
  const allRequirementsMet = requirementsMet === 4;

  const {
    control,
    handleSubmit,
    watch,
    formState: { errors },
    setError,
  } = useForm<FormData>({
    resolver: zodResolver(passwordSchema),
    defaultValues: {
      password: "",
      confirmPassword: "",
    },
  });

  // Watch password for requirements validation
  const password = watch("password");

  // Check password requirements on change
  useEffect(() => {
    if (password) {
      setRequirements({
        length: password.length >= 8 && password.length <= 20,
        uppercase: /[A-Z]/.test(password),
        number: /[0-9]/.test(password),
        special: /[!@#$%^&*(),.?":{}|<>]/.test(password),
      });
    } else {
      setRequirements({
        length: false,
        uppercase: false,
        number: false,
        special: false,
      });
    }
  }, [password]);

  const onSubmit = async (data: FormData) => {
    // Check if all requirements are met
    if (!allRequirementsMet) {
      setError("password", {
        message: "Password must meet all requirements",
      });
      return;
    }

    // Start submission
    setIsSubmitting(true);

    try {
      // Simulate API call
      await new Promise((resolve) => setTimeout(resolve, 1500));

      // Show success screen
      router.push("/passwordResetSuccess");
    } catch (error) {
      console.error("Error resetting password:", error);
    } finally {
      setIsSubmitting(false);
    }
  };

  const goBack = () => {
    router.back();
  };

  return (
    <KeyboardAvoidingView
      behavior={Platform.OS === "ios" ? "padding" : "height"}
      style={styles.container}
      keyboardVerticalOffset={Platform.OS === "ios" ? 0 : 20}
    >
      <View style={styles.innerContainer}>
        <TouchableOpacity onPress={goBack} style={styles.backButton}>
          <Ionicons name="chevron-back" size={24} color="#868D96" />
        </TouchableOpacity>

        <Animated.View
          entering={FadeInDown.delay(200).springify()}
          style={styles.header}
        >
          <Text style={styles.title}>Reset Password</Text>
          <Text style={styles.subtitle}>
            Please create a new password for your account
          </Text>
        </Animated.View>

        <Animated.View entering={FadeInDown.delay(400).springify()}>
          <PasswordInput
            control={control}
            name="password"
            label="New Password"
            placeholder="Enter your new password"
            error={errors.password}
            disabled={isSubmitting}
          />

          <PasswordInput
            control={control}
            name="confirmPassword"
            label="Confirm Password"
            placeholder="Re-enter your new password"
            error={errors.confirmPassword}
            disabled={isSubmitting}
          />

          <View style={styles.requirementsContainer}>
            <Text style={styles.requirementsTitle}>
              Your password must contain
            </Text>

            <PasswordRequirement
              label="Between 8-20 characters"
              isMet={requirements.length}
            />

            <PasswordRequirement
              label="1 upper case letter"
              isMet={requirements.uppercase}
            />

            <PasswordRequirement
              label="1 or more numbers"
              isMet={requirements.number}
            />

            <PasswordRequirement
              label="1 or more special characters"
              isMet={requirements.special}
            />
          </View>

          <TouchableOpacity
            style={[
              styles.resetButton,
              (!allRequirementsMet || isSubmitting) &&
                styles.resetButtonDisabled,
            ]}
            onPress={handleSubmit(onSubmit)}
            disabled={!allRequirementsMet || isSubmitting}
          >
            {isSubmitting ? (
              <ActivityIndicator color="white" size="small" />
            ) : (
              <Text style={styles.resetButtonText}>Reset Password</Text>
            )}
          </TouchableOpacity>
        </Animated.View>
      </View>
    </KeyboardAvoidingView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  innerContainer: {
    flex: 1,
    paddingHorizontal: 20,
    paddingTop: 20,
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
    fontSize: 16,
    fontFamily: "Poppins-Regular",
    color: "#868D96", // meristem-grey-300
    lineHeight: 24,
  },
  requirementsContainer: {
    marginTop: 16,
    marginBottom: 32,
  },
  requirementsTitle: {
    fontSize: 16,
    fontFamily: "Poppins-Medium",
    color: "#4B5563", // meristem-grey-500
    marginBottom: 16,
  },
  resetButton: {
    backgroundColor: "#00643C", // meristem-green
    borderRadius: 50,
    paddingVertical: 16,
    alignItems: "center",
    justifyContent: "center",
    marginBottom: 24,
  },
  resetButtonDisabled: {
    backgroundColor: "#ACB1B7", // meristem-grey-200
  },
  resetButtonText: {
    color: "white",
    fontFamily: "Poppins-Medium",
    fontSize: 16,
  },
});

export default ResetPasswordForm;
