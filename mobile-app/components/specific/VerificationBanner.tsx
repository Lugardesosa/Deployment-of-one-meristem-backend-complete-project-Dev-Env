import React from "react";
import { View, Text, TouchableOpacity } from "react-native";
import { Svg, Path } from "react-native-svg";
import Animated, { FadeInDown } from "react-native-reanimated";

const WarningIcon = () => (
  <Svg width={24} height={24} viewBox="0 0 24 24" fill="none">
    <Path
      d="M12 22c5.523 0 10-4.477 10-10S17.523 2 12 2 2 6.477 2 12s4.477 10 10 10z"
      stroke="#FFA500"
      strokeWidth={2}
      strokeLinecap="round"
      strokeLinejoin="round"
    />
    <Path
      d="M12 8v4M12 16h.01"
      stroke="#FFA500"
      strokeWidth={2}
      strokeLinecap="round"
      strokeLinejoin="round"
    />
  </Svg>
);

export const VerificationBanner = () => {
  return (
    <Animated.View
      entering={FadeInDown.springify()}
      className="bg-warning-light m-4 p-4 rounded-2xl"
    >
      <View className="flex-row items-center">
        <WarningIcon />
        <View className="ml-3 flex-1">
          <Text className="text-warning-dark font-poppins-medium text-base mb-1">
            Complete Your Verification
          </Text>
          <Text className="text-warning font-poppins-regular text-sm">
            Please complete your account verification to unlock all features
          </Text>
        </View>
        <TouchableOpacity className="bg-warning-dark px-4 py-2 rounded-full">
          <Text className="text-white font-poppins-medium text-sm">
            Verify Now
          </Text>
        </TouchableOpacity>
      </View>
    </Animated.View>
  );
};

export default VerificationBanner;
