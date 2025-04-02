import React from "react";
import { View } from "react-native";
import { Stack, useLocalSearchParams } from "expo-router";
import { StatusBar } from "expo-status-bar";
import { SafeAreaView } from "react-native-safe-area-context";
import OtpVerificationForm from "../../components/specific/OtpVerificationForm";

export default function VerifyForgotPasswordOtp() {
  const { email = "", isForgotPassword = "false" } = useLocalSearchParams<{
    email: string;
    isForgotPassword: string;
  }>();

  return (
    <SafeAreaView className="flex-1 bg-white">
      <StatusBar style="dark" />
      <Stack.Screen
        options={{
          title: "",
          headerShown: false,
          animation: "slide_from_right",
        }}
      />
      <OtpVerificationForm
        email={email}
        isForgotPassword={isForgotPassword === "true"}
        nextRoute="/resetPassword"
      />
    </SafeAreaView>
  );
}
