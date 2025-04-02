import React, { useState } from "react";
import {
  View,
  Text,
  TextInput,
  TextInputProps,
  Image,
  TouchableOpacity,
  StyleSheet,
} from "react-native";
import {
  Controller,
  Control,
  FieldValues,
  Path,
  FieldError,
} from "react-hook-form";
import { Ionicons } from "@expo/vector-icons";

interface PhoneInputProps<T extends FieldValues> extends TextInputProps {
  name: Path<T>;
  control: Control<T>;
  label?: string;
  error?: FieldError;
  countryCode: string;
  onCountryPress?: () => void;
  disabled?: boolean;
}

export const PhoneInput = <T extends FieldValues>({
  name,
  control,
  label,
  error,
  countryCode,
  onCountryPress,
  disabled = false,
  ...textInputProps
}: PhoneInputProps<T>) => {
  const [isFocused, setIsFocused] = useState(false);

  // Get shadow style
  const getShadowStyle = () => {
    if (error) {
      return styles.errorContainer;
    } else if (isFocused) {
      return styles.focusedContainer;
    }
    return null;
  };

  // Handle input states
  const getBorderStyle = () => {
    if (error) {
      return "border-[rgba(255,0,0,0.5)]"; // Error state with 50% opacity
    } else if (isFocused) {
      return "border-[rgba(0,100,60,0.5)]"; // Active state with 50% opacity
    } else if (disabled) {
      return "border-meristem-grey-200"; // Disabled state
    }
    return "border-meristem-grey-100"; // Default state
  };

  const getCountryBorderStyle = () => {
    if (error) {
      return "border-[rgba(255,0,0,0.5)]"; // Error state with 50% opacity
    } else if (isFocused) {
      return "border-[rgba(0,100,60,0.5)]"; // Active state with 50% opacity
    } else if (disabled) {
      return "border-meristem-grey-200"; // Disabled state
    }
    return "border-meristem-grey-100"; // Default state
  };

  return (
    <View className="mb-4">
      {label && (
        <Text
          className={`text-base mb-2 ${
            disabled ? "text-meristem-grey-200" : "text-meristem-grey-500"
          } font-poppins-regular`}
        >
          {label}
        </Text>
      )}

      <View className="flex-row">
        <TouchableOpacity
          style={getShadowStyle()}
          onPress={disabled ? undefined : onCountryPress}
          className={`flex-row items-center bg-meristem-grey-50 rounded-l-lg border px-3 ${getCountryBorderStyle()} border-r-0`}
          disabled={disabled}
        >
          <View className="mr-1">
            <Image
              source={require("../../assets/images/nigeria-flag.png")}
              className="w-6 h-6 rounded-full"
              defaultSource={require("../../assets/images/nigeria-flag.png")}
            />
          </View>
          <Text
            className={`font-poppins-medium ${
              disabled ? "text-meristem-grey-200" : "text-meristem-grey-500"
            }`}
          >
            {countryCode}
          </Text>
          <Ionicons
            name="chevron-down"
            size={16}
            color={disabled ? "#ACB1B7" : "#4b5563"}
          />
        </TouchableOpacity>

        <Controller
          control={control}
          name={name}
          render={({ field: { onChange, onBlur, value } }) => (
            <TextInput
              style={getShadowStyle()}
              className={`flex-1 bg-neutral-white border-t border-r border-b rounded-r-lg p-3 font-poppins-regular text-base ${getBorderStyle()} border-l-0 ${
                disabled ? "text-meristem-grey-200" : "text-meristem-grey-900"
              }`}
              onChangeText={onChange}
              onBlur={() => {
                setIsFocused(false);
                onBlur();
              }}
              onFocus={() => setIsFocused(true)}
              value={value}
              keyboardType="phone-pad"
              editable={!disabled}
              placeholderTextColor={disabled ? "#ACB1B7" : "#868D96"}
              {...textInputProps}
            />
          )}
        />
      </View>

      {error && (
        <Text className="text-error font-poppins-regular text-sm mt-1">
          {error.message}
        </Text>
      )}
    </View>
  );
};

// Add shadow styles according to Figma specifications
const styles = StyleSheet.create({
  focusedContainer: {
    shadowColor: "#00643C", // meristem-green color
    shadowOffset: { width: 0, height: 0 },
    shadowOpacity: 0.15, // 26% opacity approximation
    shadowRadius: 4.5, // 9px/2 approximation
    elevation: 3, // Android shadow
  },
  errorContainer: {
    shadowColor: "#FF0000", // error color
    shadowOffset: { width: 0, height: 0 },
    shadowOpacity: 0.13, // 21% opacity approximation
    shadowRadius: 2, // 4px/2 approximation
    elevation: 2, // Android shadow
  },
});

export default PhoneInput;
