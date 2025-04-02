import ResetPasswordForm from "@/components/specific/ResetPasswordForm";
import { Stack } from "expo-router";
import { StatusBar } from "expo-status-bar";
import React from "react";
import { SafeAreaView } from "react-native-safe-area-context";

export default function ResetPassword() {
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
      <ResetPasswordForm />
    </SafeAreaView>
  );
}
