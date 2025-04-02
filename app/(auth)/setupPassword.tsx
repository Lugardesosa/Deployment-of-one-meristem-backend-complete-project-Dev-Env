import React from "react";
import { View } from "react-native";
import { Stack } from "expo-router";
import { StatusBar } from "expo-status-bar";
import PasswordSetupForm from "../../components/specific/PasswordSetupForm";

export default function SetupPassword() {
  return (
    <View style={{ flex: 1, backgroundColor: "white" }}>
      <StatusBar style="dark" />
      <Stack.Screen
        options={{
          title: "",
          headerShadowVisible: false,
          headerStyle: { backgroundColor: "white" },
        }}
      />
      <PasswordSetupForm />
    </View>
  );
}
