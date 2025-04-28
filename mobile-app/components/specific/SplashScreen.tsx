import React from "react";
import { View, Image } from "react-native";
import { LinearGradient } from "expo-linear-gradient";

interface SplashScreenProps {
  onAnimationComplete?: () => void;
}

export const SplashScreen: React.FC<SplashScreenProps> = ({
  onAnimationComplete,
}) => {
  // The onAnimationComplete prop will be used later if we need to trigger a navigation
  // after the splash screen animation finishes

  return (
    <View className="flex-1 justify-center items-center bg-meristem-green">
      <Image
        source={require("../../assets/images/meristem-logo.png")}
        className="w-64 h-16 object-contain"
        resizeMode="contain"
      />
    </View>
  );
};

export default SplashScreen;
