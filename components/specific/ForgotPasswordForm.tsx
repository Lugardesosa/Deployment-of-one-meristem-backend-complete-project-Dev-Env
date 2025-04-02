import React, { useState } from "react";
import { View, Text, TouchableOpacity, StyleSheet } from "react-native";
import { useRouter } from "expo-router";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import FormInput from "../common/FormInput";
import Animated, { FadeIn, FadeInDown } from "react-native-reanimated";
import { Ionicons } from "@expo/vector-icons";
import { KeyboardAvoidingView, Platform } from "react-native";

const forgotPasswordSchema = z.object({
  email: z.string().email({ message: "Please enter a valid email address" }),
});

type ForgotPasswordFormData = z.infer<typeof forgotPasswordSchema>;

export const ForgotPasswordForm = () => {
  const router = useRouter();
  const [isSubmitting, setIsSubmitting] = useState(false);

  const {
    control,
    handleSubmit,
    formState: { errors, isValid },
  } = useForm<ForgotPasswordFormData>({
    resolver: zodResolver(forgotPasswordSchema),
    mode: "onChange",
    defaultValues: {
      email: "",
    },
  });

  const onSubmit = async (data: ForgotPasswordFormData) => {
    setIsSubmitting(true);
    try {
      // Simulate API call
      await new Promise((resolve) => setTimeout(resolve, 1500));

      // Navigate to OTP verification
      router.push({
        pathname: "/verifyForgotPasswordOtp",
        params: { email: data.email, isForgotPassword: "true" },
      });
    } catch (error) {
      console.error("Error requesting password reset:", error);
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
          className="mb-4"
        >
          <Text className="text-[32px] font-poppins-semibold text-meristem-grey-900 mb-2">
            Forgot password
          </Text>
          <Text className="text-base font-poppins-regular text-meristem-grey-500 mb-8">
            A password reset code will be sent to the email address entered
            below.
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

          <TouchableOpacity
            onPress={handleSubmit(onSubmit)}
            disabled={!isValid || isSubmitting}
            className={`rounded-full py-4 w-full mt-8 ${
              !isValid || isSubmitting
                ? "bg-meristem-grey-200"
                : "bg-meristem-green"
            }`}
          >
            <Text className="text-white font-poppins-medium text-center text-base">
              {isSubmitting ? "Sending..." : "Send Code"}
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
});

export default ForgotPasswordForm;
