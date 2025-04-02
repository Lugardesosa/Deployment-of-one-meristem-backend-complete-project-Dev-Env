import React, { useEffect } from "react";
import { View, Text, TouchableOpacity, StyleSheet } from "react-native";
import Animated, {
  FadeIn,
  FadeInDown,
  SlideInDown,
  withSpring,
  useAnimatedStyle,
  useSharedValue,
} from "react-native-reanimated";
import { Svg, Path, Circle } from "react-native-svg";

interface SuccessScreenProps {
  title: string;
  description?: string;
  buttonText: string;
  onButtonPress: () => void;
}

const SuccessIcon = () => {
  return (
    <Svg width={80} height={80} viewBox="0 0 80 80" fill="none">
      <Circle cx={40} cy={40} r={40} fill="#00643C" />
      <Circle cx={40} cy={40} r={32} fill="#E6F0EC" />
      <Path
        d="M32 40l6 6 12-12"
        stroke="#00643C"
        strokeWidth={3}
        strokeLinecap="round"
        strokeLinejoin="round"
      />
    </Svg>
  );
};

export const SuccessScreen: React.FC<SuccessScreenProps> = ({
  title,
  description,
  buttonText,
  onButtonPress,
}) => {
  const scale = useSharedValue(0.3);

  useEffect(() => {
    scale.value = withSpring(1, {
      damping: 8,
      stiffness: 100,
    });
  }, []);

  const iconStyle = useAnimatedStyle(() => {
    return {
      transform: [{ scale: scale.value }],
    };
  });

  return (
    <View className="flex-1 bg-meristem-green-light justify-center items-center px-6">
      <Animated.View style={iconStyle} className="mb-8">
        <SuccessIcon />
      </Animated.View>

      <Animated.View
        entering={FadeInDown.delay(400).springify()}
        className="items-center"
      >
        <Text className="text-[32px] font-poppins-semibold text-meristem-grey-900 text-center mb-2">
          {title}
        </Text>
        {description && (
          <Text className="text-base font-poppins-regular text-meristem-grey-500 text-center mb-8">
            {description}
          </Text>
        )}
      </Animated.View>

      <Animated.View
        entering={SlideInDown.delay(600).springify()}
        className="w-full mt-6"
      >
        <TouchableOpacity
          onPress={onButtonPress}
          className="bg-meristem-green rounded-full py-4 px-6 w-full"
        >
          <Text className="text-white font-poppins-medium text-center text-base">
            {buttonText}
          </Text>
        </TouchableOpacity>
      </Animated.View>
    </View>
  );
};

export default SuccessScreen;
