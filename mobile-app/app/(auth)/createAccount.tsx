import React from "react";
import { View } from "react-native";
import { Stack } from "expo-router";
import CreateAccountForm from "../../components/specific/CreateAccountForm";

export default function CreateAccount() {
  return (
    <View className="flex-1 bg-white">
      <Stack.Screen
        options={{
          headerShown: false,
          animation: "slide_from_right",
        }}
      />
      <CreateAccountForm />
    </View>
  );
}
