import React from "react";
import {
  View,
  Text,
  Image,
  ImageSourcePropType,
  Dimensions,
} from "react-native";
import { LinearGradient } from "expo-linear-gradient";

const { width, height } = Dimensions.get("window");

interface OnboardingSlideProps {
  image: ImageSourcePropType;
  title: string;
  description: string;
}

export const OnboardingSlide: React.FC<OnboardingSlideProps> = ({
  image,
  title,
  description,
}) => {
  return (
    <View className="flex-1 w-full" style={{ width }}>
      <View className="flex-1 relative">
        <Image
          source={image}
          className="absolute top-0 left-0 right-0 bottom-0 w-full h-full"
          style={{ width, height }}
          resizeMode="cover"
        />

        {/* Overlay gradient - matches Figma spec */}
        <LinearGradient
          colors={["rgba(0, 0, 0, 0)", "#00643C"]}
          locations={[0.4, 1]}
          className="absolute top-0 left-0 right-0 bottom-0"
          style={{ width, height }}
          start={{ x: 0.5, y: 0 }}
          end={{ x: 0.5, y: 1 }}
        />
      </View>

      <View className="absolute bottom-24 left-0 right-0 px-5">
        <Text className="text-[31px] font-poppins-bold text-white mb-4">
          {title}
        </Text>
        <Text className="text-sm font-poppins-regular text-white">
          {description}
        </Text>
      </View>
    </View>
  );
};

export default OnboardingSlide;
