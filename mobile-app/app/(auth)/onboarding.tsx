import React from "react";
import { View } from "react-native";
import { Stack } from "expo-router";
import OnboardingContainer from "../../components/specific/OnboardingContainer";

export default function Onboarding() {
  return (
    <View className="flex-1">
      <Stack.Screen options={{ headerShown: false }} />
      <OnboardingContainer />
    </View>
  );
}
