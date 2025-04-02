import React, { useState } from "react";
import { View, Text, TouchableOpacity, StyleSheet } from "react-native";
import { useRouter } from "expo-router";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import FormInput from "../common/FormInput";
import PasswordInput from "../common/PasswordInput";
import { Svg, Path } from "react-native-svg";
import * as LocalAuthentication from "expo-local-authentication";
import Animated, {
  FadeIn,
  FadeInDown,
  SlideInDown,
} from "react-native-reanimated";
import { useSafeAreaInsets } from "react-native-safe-area-context";
import { KeyboardAvoidingView, Platform } from "react-native";

const signInSchema = z.object({
  email: z.string().email({ message: "Please enter a valid email address" }),
  password: z.string().min(1, { message: "Password is required" }),
});

type SignInFormData = z.infer<typeof signInSchema>;

const BiometricIcon = () => (
  <Svg width={24} height={24} viewBox="0 0 24 24" fill="none">
    <Path
      d="M12 2C6.477 2 2 6.477 2 12s4.477 10 10 10 10-4.477 10-10S17.523 2 12 2zm0 18c-4.418 0-8-3.582-8-8s3.582-8 8-8 8 3.582 8 8-3.582 8-8 8z"
      fill="#868D96"
    />
    <Path
      d="M12 6c-3.309 0-6 2.691-6 6s2.691 6 6 6 6-2.691 6-6-2.691-6-6-6zm0 10c-2.206 0-4-1.794-4-4s1.794-4 4-4 4 1.794 4 4-1.794 4-4 4z"
      fill="#868D96"
    />
    <Path
      d="M12 10c-1.103 0-2 .897-2 2s.897 2 2 2 2-.897 2-2-.897-2-2-2z"
      fill="#868D96"
    />
  </Svg>
);

export const SignInForm = () => {
  const router = useRouter();
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [isBiometricSupported, setIsBiometricSupported] = useState(false);
  const insets = useSafeAreaInsets();

  const {
    control,
    handleSubmit,
    formState: { errors, isValid },
  } = useForm<SignInFormData>({
    resolver: zodResolver(signInSchema),
    mode: "onChange",
    defaultValues: {
      email: "",
      password: "",
    },
  });

  React.useEffect(() => {
    checkBiometricSupport();
  }, []);

  const checkBiometricSupport = async () => {
    const compatible = await LocalAuthentication.hasHardwareAsync();
    setIsBiometricSupported(compatible);
  };

  const handleBiometricAuth = async () => {
    try {
      const result = await LocalAuthentication.authenticateAsync({
        promptMessage: "Sign in with biometrics",
        fallbackLabel: "Use password",
      });

      if (result.success) {
        // Simulate successful biometric auth
        handleSuccessfulSignIn();
      }
    } catch (error) {
      console.error("Biometric authentication error:", error);
    }
  };

  const onSubmit = async (data: SignInFormData) => {
    setIsSubmitting(true);
    try {
      // Simulate API call
      await new Promise((resolve) => setTimeout(resolve, 1500));
      handleSuccessfulSignIn();
    } catch (error) {
      console.error("Sign in error:", error);
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleSuccessfulSignIn = () => {
    router.replace("/dashboard");
  };

  const goToCreateAccount = () => {
    router.push("/createAccount");
  };

  const goToForgotPassword = () => {
    // Navigate to forgot password
    router.push("/forgotPassword");
  };

  return (
    <KeyboardAvoidingView
      behavior={Platform.OS === "ios" ? "padding" : "height"}
      style={styles.container}
      keyboardVerticalOffset={Platform.OS === "ios" ? 0 : 20}
    >
      <View style={styles.innerContainer}>
        <Animated.View
          entering={FadeInDown.delay(200).springify()}
          className="mb-8"
        >
          <Text className="text-[40px] font-poppins-semibold text-meristem-grey-900 mb-2">
            Sign In
          </Text>
        </Animated.View>

        <Animated.View entering={FadeInDown.delay(400).springify()}>
          <FormInput
            control={control}
            name="email"
            label="Email address"
            placeholder="Enter your email address"
            error={errors.email}
            keyboardType="email-address"
            autoCapitalize="none"
            autoCorrect={false}
            disabled={isSubmitting}
          />

          <PasswordInput
            control={control}
            name="password"
            label="Password"
            placeholder="Enter your password"
            error={errors.password}
            disabled={isSubmitting}
          />

          <TouchableOpacity
            onPress={goToForgotPassword}
            className="self-end mb-6"
          >
            <Text className="text-meristem-grey-500 font-poppins-regular">
              Forgot password?
            </Text>
          </TouchableOpacity>

          <TouchableOpacity
            onPress={handleSubmit(onSubmit)}
            disabled={!isValid || isSubmitting}
            className={`rounded-full py-4 w-full mb-4 ${
              !isValid || isSubmitting
                ? "bg-meristem-grey-200"
                : "bg-meristem-green"
            }`}
          >
            <Text className="text-white font-poppins-medium text-center text-base">
              {isSubmitting ? "Signing in..." : "Continue"}
            </Text>
          </TouchableOpacity>

          {isBiometricSupported && (
            <TouchableOpacity
              onPress={handleBiometricAuth}
              className="flex-row justify-center items-center py-4"
            >
              <BiometricIcon />
              <Text className="text-meristem-grey-500 font-poppins-regular ml-2">
                Sign in with biometrics
              </Text>
            </TouchableOpacity>
          )}
        </Animated.View>

        <View style={{ flex: 1 }} />

        <Animated.View
          entering={SlideInDown.delay(600).springify()}
          className="flex-row justify-center items-center py-4"
        >
          <Text className="text-meristem-grey-500 font-poppins-regular">
            New here?{" "}
          </Text>
          <TouchableOpacity onPress={goToCreateAccount}>
            <Text className="text-meristem-green font-poppins-medium">
              Create an account
            </Text>
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
    paddingBottom: 16,
    justifyContent: "space-between",
  },
});

export default SignInForm;
