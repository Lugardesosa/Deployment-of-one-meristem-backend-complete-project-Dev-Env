import React, { useState } from "react";
import {
  View,
  Text,
  TextInput,
  TextInputProps,
  StyleSheet,
} from "react-native";
import {
  Controller,
  Control,
  FieldValues,
  Path,
  FieldError,
} from "react-hook-form";

interface FormInputProps<T extends FieldValues> extends TextInputProps {
  name: Path<T>;
  control: Control<T>;
  label?: string;
  error?: FieldError;
  disabled?: boolean;
}

export const FormInput = <T extends FieldValues>({
  name,
  control,
  label,
  error,
  disabled = false,
  ...textInputProps
}: FormInputProps<T>) => {
  const [isFocused, setIsFocused] = useState(false);

  // Get combined styles for TextInput
  const getInputStyle = () => {
    let shadowStyle = null;
    if (error) {
      shadowStyle = styles.errorContainer;
    } else if (isFocused) {
      shadowStyle = styles.focusedContainer;
    }

    return shadowStyle;
  };

  // Handle border styles
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
      {label && (
        <Text
          className={`text-base mb-2 ${
            disabled ? "text-meristem-grey-200" : "text-meristem-grey-500"
          } font-poppins-regular`}
        >
          {label}
        </Text>
      )}

      <Controller
        control={control}
        name={name}
        render={({ field: { onChange, onBlur, value } }) => (
          <TextInput
            style={getInputStyle()}
            className={`bg-neutral-white border rounded-lg p-3 font-poppins-regular text-base ${getBorderStyle()} ${
              disabled ? "text-meristem-grey-200" : "text-meristem-grey-900"
            }`}
            onChangeText={onChange}
            onBlur={() => {
              setIsFocused(false);
              onBlur();
            }}
            onFocus={() => setIsFocused(true)}
            value={value}
            editable={!disabled}
            placeholderTextColor={disabled ? "#ACB1B7" : "#868D96"} // meristem-grey-200 or 300
            {...textInputProps}
          />
        )}
      />

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
    shadowOpacity: 0.33, // 21% opacity approximation
    shadowRadius: 2, // 4px/2 approximation
    elevation: 2, // Android shadow
  },
});

export default FormInput;
