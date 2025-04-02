import React, { useRef, useState, useEffect } from "react";
import {
  View,
  TextInput,
  StyleSheet,
  Keyboard,
  NativeSyntheticEvent,
  TextInputKeyPressEventData,
  Clipboard,
} from "react-native";
import Animated, {
  useSharedValue,
  useAnimatedStyle,
  withTiming,
  withSequence,
  Easing,
} from "react-native-reanimated";

interface OtpInputProps {
  length: number;
  value: string;
  onChange: (value: string) => void;
  autoFocus?: boolean;
}

const OtpInput: React.FC<OtpInputProps> = ({
  length,
  value,
  onChange,
  autoFocus = true,
}) => {
  const inputRefs = useRef<(TextInput | null)[]>([]);
  const [focused, setFocused] = useState<number>(-1);
  const shakeAnimation = useSharedValue(0);

  // Initialize an array of refs based on length
  useEffect(() => {
    inputRefs.current = Array(length).fill(null);
  }, [length]);

  // Listen for clipboard paste
  useEffect(() => {
    const checkClipboard = async () => {
      try {
        const text = await Clipboard.getString();
        // Check if clipboard contains only numbers and matches the expected length
        if (/^\d+$/.test(text) && text.length === length) {
          onChange(text);
        }
      } catch (error) {
        console.log("Error reading clipboard:", error);
      }
    };

    if (autoFocus && value.length === 0) {
      checkClipboard();
    }
  }, []);

  const handleKeyPress = (
    e: NativeSyntheticEvent<TextInputKeyPressEventData>,
    index: number
  ) => {
    if (e.nativeEvent.key === "Backspace" && index > 0 && !value[index]) {
      // Move focus to previous input on backspace if current input is empty
      inputRefs.current[index - 1]?.focus();
    }
  };

  const handleChange = (text: string, index: number) => {
    // Only allow numbers
    if (!/^\d*$/.test(text)) {
      triggerShakeAnimation();
      return;
    }

    const newValue = [...value];

    // Handle pasting multiple digits
    if (text.length > 1) {
      const pastedText = text.substring(0, length - index);
      for (let i = 0; i < pastedText.length; i++) {
        if (index + i < length) {
          newValue[index + i] = pastedText[i];
        }
      }
      onChange(newValue.join(""));

      // Focus on the next empty input or the last one
      const nextIndex = Math.min(index + pastedText.length, length - 1);
      inputRefs.current[nextIndex]?.focus();
      return;
    }

    // Handle single digit input
    newValue[index] = text;
    onChange(newValue.join(""));

    // Auto focus on next input if available
    if (text && index < length - 1) {
      inputRefs.current[index + 1]?.focus();
    }
  };

  const handleFocus = (index: number) => {
    setFocused(index);
  };

  const handleBlur = () => {
    setFocused(-1);
  };

  const triggerShakeAnimation = () => {
    shakeAnimation.value = withSequence(
      withTiming(-3, { duration: 50, easing: Easing.inOut(Easing.quad) }),
      withTiming(3, { duration: 50, easing: Easing.inOut(Easing.quad) }),
      withTiming(-3, { duration: 50, easing: Easing.inOut(Easing.quad) }),
      withTiming(3, { duration: 50, easing: Easing.inOut(Easing.quad) }),
      withTiming(0, { duration: 50, easing: Easing.inOut(Easing.quad) })
    );
  };

  const animatedStyle = useAnimatedStyle(() => {
    return {
      transform: [{ translateX: shakeAnimation.value }],
    };
  });

  const getBorderColor = (index: number) => {
    if (value[index]) {
      return "#00643C"; // Filled (meristem-green)
    } else if (focused === index) {
      return "rgba(0, 100, 60, 0.5)"; // Focused with 50% opacity
    }
    return "#E5E5E5"; // Default border
  };

  return (
    <Animated.View style={[styles.container, animatedStyle]}>
      {Array(length)
        .fill(0)
        .map((_, index) => (
          <View
            key={index}
            style={[
              styles.inputContainer,
              { borderColor: getBorderColor(index) },
            ]}
          >
            <TextInput
              ref={(el) => (inputRefs.current[index] = el)}
              value={value[index] || ""}
              onChangeText={(text) => handleChange(text, index)}
              onKeyPress={(e) => handleKeyPress(e, index)}
              onFocus={() => handleFocus(index)}
              onBlur={handleBlur}
              style={styles.input}
              keyboardType="numeric"
              maxLength={length}
              autoFocus={autoFocus && index === 0}
              caretHidden={true}
              selectTextOnFocus
            />
          </View>
        ))}
    </Animated.View>
  );
};

const styles = StyleSheet.create({
  container: {
    flexDirection: "row",
    justifyContent: "space-between",
    width: "100%",
  },
  inputContainer: {
    width: 70,
    height: 70,
    borderRadius: 15,
    borderWidth: 1,
    justifyContent: "center",
    alignItems: "center",
    backgroundColor: "white",
  },
  input: {
    fontSize: 24,
    textAlign: "center",
    fontFamily: "Poppins-Medium",
    width: "100%",
    height: "100%",
  },
});

export default OtpInput;
