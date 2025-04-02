import React, { useState } from "react";
import {
  View,
  Text,
  TextInput,
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

interface PasswordInputProps<T extends FieldValues> {
  name: Path<T>;
  control: Control<T>;
  label: string;
  placeholder: string;
  error?: FieldError;
  secureTextEntry?: boolean;
  disabled?: boolean;
}

export const PasswordInput = <T extends FieldValues>({
  name,
  control,
  label,
  placeholder,
  error,
  secureTextEntry = true,
  disabled = false,
}: PasswordInputProps<T>) => {
  const [isFocused, setIsFocused] = useState(false);
  const [passwordVisible, setPasswordVisible] = useState(false);

  const togglePasswordVisibility = () => {
    setPasswordVisible((prev) => !prev);
  };

  // Get container style with shadows
  const getContainerStyles = () => {
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

  return (
    <View className="mb-4">
      <Text
        className={`text-base mb-2 ${
          disabled ? "text-meristem-grey-200" : "text-meristem-grey-500"
        } font-poppins-regular`}
      >
        {label}
      </Text>

      <View
        style={getContainerStyles()}
        className={`bg-neutral-white border rounded-lg overflow-hidden flex-row items-center ${getBorderStyle()}`}
      >
        <Controller
          control={control}
          name={name}
          render={({ field: { onChange, onBlur, value } }) => (
            <TextInput
              className={`flex-1 py-4 px-4 font-poppins-regular text-base ${
                disabled ? "text-meristem-grey-200" : "text-meristem-grey-900"
              }`}
              placeholder={placeholder}
              placeholderTextColor={disabled ? "#ACB1B7" : "#868D96"}
              secureTextEntry={secureTextEntry && !passwordVisible}
              value={value}
              onChangeText={onChange}
              onFocus={() => setIsFocused(true)}
              onBlur={() => {
                setIsFocused(false);
                onBlur();
              }}
              editable={!disabled}
            />
          )}
        />

        <TouchableOpacity
          className="pr-4"
          onPress={togglePasswordVisibility}
          disabled={disabled}
        >
          <Ionicons
            name={passwordVisible ? "eye-off-outline" : "eye-outline"}
            size={24}
            color={disabled ? "#ACB1B7" : "#868D96"}
          />
        </TouchableOpacity>
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

export default PasswordInput;
