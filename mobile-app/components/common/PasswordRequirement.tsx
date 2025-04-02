import React, { useEffect } from "react";
import { View, Text, StyleSheet } from "react-native";
import Animated, {
  useSharedValue,
  useAnimatedStyle,
  withTiming,
  Easing,
} from "react-native-reanimated";
import { Ionicons } from "@expo/vector-icons";

interface PasswordRequirementProps {
  label: string;
  isMet: boolean;
}

const PasswordRequirement: React.FC<PasswordRequirementProps> = ({
  label,
  isMet,
}) => {
  const scale = useSharedValue(1);
  const opacity = useSharedValue(0);

  useEffect(() => {
    if (isMet) {
      scale.value = withTiming(1.2, {
        duration: 150,
        easing: Easing.bezier(0.25, 0.1, 0.25, 1),
      });
      opacity.value = withTiming(1, {
        duration: 300,
        easing: Easing.bezier(0.25, 0.1, 0.25, 1),
      });

      setTimeout(() => {
        scale.value = withTiming(1, {
          duration: 150,
          easing: Easing.bezier(0.25, 0.1, 0.25, 1),
        });
      }, 150);
    } else {
      opacity.value = withTiming(0, {
        duration: 300,
        easing: Easing.bezier(0.25, 0.1, 0.25, 1),
      });
    }
  }, [isMet]);

  const iconStyle = useAnimatedStyle(() => {
    return {
      transform: [{ scale: scale.value }],
      opacity: opacity.value,
    };
  });

  return (
    <View style={styles.container}>
      <View style={styles.iconContainer}>
        {isMet ? (
          <Animated.View style={iconStyle}>
            <Ionicons name="checkmark-circle" size={24} color="#00643C" />
          </Animated.View>
        ) : (
          <View style={styles.circle} />
        )}
      </View>
      <Text style={[styles.label, isMet && styles.metLabel]}>{label}</Text>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flexDirection: "row",
    alignItems: "center",
    marginBottom: 12,
  },
  iconContainer: {
    width: 24,
    height: 24,
    justifyContent: "center",
    alignItems: "center",
    marginRight: 12,
  },
  circle: {
    width: 24,
    height: 24,
    borderRadius: 12,
    borderWidth: 2,
    borderColor: "#ACB1B7", // meristem-grey-200
  },
  label: {
    fontFamily: "Poppins-Regular",
    fontSize: 14,
    color: "#868D96", // meristem-grey-300
  },
  metLabel: {
    color: "#4B5563", // meristem-grey-500
    fontFamily: "Poppins-Medium",
  },
});

export default PasswordRequirement;
