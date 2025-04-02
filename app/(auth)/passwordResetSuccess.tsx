import React from "react";
import { Stack, useRouter } from "expo-router";
import { StatusBar } from "expo-status-bar";
import SuccessScreen from "../../components/common/SuccessScreen";

export default function PasswordResetSuccess() {
  const router = useRouter();

  const handleButtonPress = () => {
    router.replace("/signin");
  };

  return (
    <>
      <StatusBar style="dark" />
      <Stack.Screen
        options={{
          headerShown: false,
          animation: "fade",
        }}
      />
      <SuccessScreen
        title="Password Reset!"
        description="Your password has been successfully reset. You can now sign in with your new password."
        buttonText="Go to Sign In"
        onButtonPress={handleButtonPress}
      />
    </>
  );
}
