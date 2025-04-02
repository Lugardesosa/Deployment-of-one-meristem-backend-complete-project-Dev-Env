import React from "react";
import { View } from "react-native";
import { Stack, useLocalSearchParams } from "expo-router";
import { StatusBar } from "expo-status-bar";
import OtpVerificationForm from "../../components/specific/OtpVerificationForm";

export default function VerifyOtp() {
  const { email = "Bibi@meristemng.com" } = useLocalSearchParams<{
    email: string;
  }>();

  return (
    <View style={{ flex: 1, backgroundColor: "white" }}>
      <StatusBar style="dark" />
      <Stack.Screen
        options={{
          title: "",
          headerShadowVisible: false,
          headerStyle: { backgroundColor: "white" },
          animation: "slide_from_right",
        }}
      />
      <OtpVerificationForm email={email} />
    </View>
  );
}
