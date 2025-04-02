import React, { useRef, useState, useEffect } from "react";
import {
  View,
  Text,
  FlatList,
  TouchableOpacity,
  Dimensions,
} from "react-native";
import { useRouter } from "expo-router";
import OnboardingSlide from "../common/OnboardingSlide";
import { Ionicons } from "@expo/vector-icons";
import Animated, {
  useSharedValue,
  useAnimatedScrollHandler,
  interpolate,
  useAnimatedStyle,
  withTiming,
  useAnimatedProps,
  Easing,
} from "react-native-reanimated";
import Svg, { Circle } from "react-native-svg";

const { width } = Dimensions.get("window");

const slides = [
  {
    key: "1",
    image: require("../../assets/images/welcome-image-one.jpg"),
    title: "Invest Smarter, Grow Your Wealth.",
    description: "Take control of your future—invest with ease and confidence.",
  },
  {
    key: "2",
    image: require("../../assets/images/welcome-image-two.jpg"),
    title: "Stay in Control of Your Wealth",
    description:
      "Secure, stress-free investing designed for stability and long-term growth.",
  },
  {
    key: "3",
    image: require("../../assets/images/welcome-image-three.jpg"),
    title: "Financial Freedom is Just a Tap Away",
    description: "Let your money grow while you focus on what matters most.",
  },
];

// Progress ring constants
const CIRCLE_SIZE = 70; // Size of the outer circle
const CIRCLE_RADIUS = CIRCLE_SIZE / 2;
const CIRCLE_STROKE_WIDTH = 2;
const BUTTON_SIZE = 58; // Size of the inner white button

// Create an animated version of the SVG Circle
const AnimatedCircle = Animated.createAnimatedComponent(Circle);

export const OnboardingContainer = () => {
  const router = useRouter();
  const flatListRef = useRef<FlatList>(null);
  const [currentIndex, setCurrentIndex] = useState(0);
  const translationX = useSharedValue(0);

  // Animated progress value (0 to 1)
  const progress = useSharedValue(0.25); // Start at 25% (90 degrees)

  // Calculate circle properties
  const circumference = 2 * Math.PI * (CIRCLE_RADIUS - CIRCLE_STROKE_WIDTH);

  useEffect(() => {
    // Map index to progress: 0->0.25 (90°), 1->0.5 (180°), 2->1 (360°)
    const targetProgress =
      currentIndex === 0 ? 0.25 : currentIndex === 1 ? 0.5 : 1;

    // Animate to the new progress value
    progress.value = withTiming(targetProgress, {
      duration: 600,
      easing: Easing.bezier(0.25, 0.1, 0.25, 1),
    });
  }, [currentIndex]);

  const scrollHandler = useAnimatedScrollHandler({
    onScroll: (event) => {
      translationX.value = event.contentOffset.x;
    },
  });

  const viewableItemsChanged = useRef(({ viewableItems }: any) => {
    if (viewableItems[0]) {
      setCurrentIndex(Number(viewableItems[0].index));
    }
  }).current;

  const viewConfig = useRef({ viewAreaCoveragePercentThreshold: 50 }).current;

  const goToNextSlide = () => {
    if (currentIndex < slides.length - 1) {
      flatListRef.current?.scrollToIndex({ index: currentIndex + 1 });
    } else {
      router.push("/createAccount");
    }
  };

  const goToSignIn = () => {
    router.push("/signin");
  };

  // Use useAnimatedProps to animate the Circle
  const animatedCircleProps = useAnimatedProps(() => {
    const strokeDashoffset = circumference * (1 - progress.value);

    return {
      strokeDashoffset,
    };
  });

  return (
    <View className="flex-1 bg-white">
      <Animated.FlatList
        ref={flatListRef}
        data={slides}
        renderItem={({ item }) => (
          <OnboardingSlide
            image={item.image}
            title={item.title}
            description={item.description}
          />
        )}
        horizontal
        showsHorizontalScrollIndicator={false}
        pagingEnabled
        bounces={false}
        onScroll={scrollHandler}
        onViewableItemsChanged={viewableItemsChanged}
        viewabilityConfig={viewConfig}
        scrollEventThrottle={16}
      />

      <View className="absolute bottom-5 left-0 right-0 px-5 flex-row justify-between items-end">
        <View className="items-start">
          {/* Dots moved to left side, above Skip button */}
          <View className="flex-row gap-2 mb-4">
            {slides.map((_, i) => {
              const dotStyle = useAnimatedStyle(() => {
                const inputRange = [
                  (i - 1) * width,
                  i * width,
                  (i + 1) * width,
                ];
                const dotWidthAnimated = interpolate(
                  translationX.value,
                  inputRange,
                  [8, 20, 8],
                  { extrapolateRight: "clamp", extrapolateLeft: "clamp" }
                );
                const opacity = interpolate(
                  translationX.value,
                  inputRange,
                  [0.3, 1, 0.3],
                  { extrapolateRight: "clamp", extrapolateLeft: "clamp" }
                );

                return {
                  width: dotWidthAnimated,
                  opacity,
                };
              });

              return (
                <Animated.View
                  key={i.toString()}
                  className="h-2 rounded-full bg-white mx-[2px]"
                  style={dotStyle}
                />
              );
            })}
          </View>

          {/* Skip button */}
          <TouchableOpacity onPress={goToSignIn} className="py-2">
            <Text className="text-white font-poppins-medium text-base">
              Skip
            </Text>
          </TouchableOpacity>
        </View>

        {/* Chevron button with progress ring */}
        <View
          className="relative"
          style={{ width: CIRCLE_SIZE, height: CIRCLE_SIZE }}
        >
          {/* SVG Progress Ring */}
          <Svg
            width={CIRCLE_SIZE}
            height={CIRCLE_SIZE}
            style={{ position: "absolute" }}
          >
            {/* Background circle */}
            <Circle
              cx={CIRCLE_RADIUS}
              cy={CIRCLE_RADIUS}
              r={CIRCLE_RADIUS - CIRCLE_STROKE_WIDTH}
              stroke="rgba(255, 255, 255, 0.3)"
              strokeWidth={CIRCLE_STROKE_WIDTH}
              fill="transparent"
            />
            {/* Animated Progress circle */}
            <AnimatedCircle
              cx={CIRCLE_RADIUS}
              cy={CIRCLE_RADIUS}
              r={CIRCLE_RADIUS - CIRCLE_STROKE_WIDTH}
              stroke="white"
              strokeWidth={CIRCLE_STROKE_WIDTH}
              fill="transparent"
              strokeDasharray={circumference}
              animatedProps={animatedCircleProps}
              strokeLinecap="round"
              transform={`rotate(-90, ${CIRCLE_RADIUS}, ${CIRCLE_RADIUS})`}
            />
          </Svg>

          {/* White button with chevron */}
          <View
            className="absolute items-center justify-center bg-white rounded-full"
            style={{
              width: BUTTON_SIZE,
              height: BUTTON_SIZE,
              top: (CIRCLE_SIZE - BUTTON_SIZE) / 2,
              left: (CIRCLE_SIZE - BUTTON_SIZE) / 2,
            }}
          >
            <TouchableOpacity
              onPress={goToNextSlide}
              className="w-full h-full justify-center items-center"
            >
              <Ionicons name="chevron-forward" size={24} color="#00643C" />
            </TouchableOpacity>
          </View>
        </View>
      </View>
    </View>
  );
};

export default OnboardingContainer;
