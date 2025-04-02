import { zodResolver } from "@hookform/resolvers/zod";
import { useRouter } from "expo-router";
import React, { useState } from "react";
import { useForm } from "react-hook-form";
import {
  ScrollView,
  Text,
  TouchableOpacity,
  View,
  Dimensions,
} from "react-native";
import { useSafeAreaInsets } from "react-native-safe-area-context";
import { z } from "zod";
import FormInput from "../common/FormInput";
import PhoneInput from "../common/PhoneInput";

const { height } = Dimensions.get("window");

const accountSchema = z.object({
  firstName: z.string().min(2, {
    message: "First name must be at least 2 characters",
  }),
  surname: z.string().min(2, {
    message: "Surname must be at least 2 characters",
  }),
  email: z.string().email({ message: "Please enter a valid email address" }),
  phone: z.string().min(8, { message: "Please enter a valid phone number" }),
  referralCode: z.string().optional(),
});

type AccountFormData = z.infer<typeof accountSchema>;

export const CreateAccountForm = () => {
  const router = useRouter();
  const [countryCode, setCountryCode] = useState("+234");
  const insets = useSafeAreaInsets();
  const [isSubmitting, setIsSubmitting] = useState(false);

  const {
    control,
    handleSubmit,
    formState: { errors, isValid },
  } = useForm<AccountFormData>({
    resolver: zodResolver(accountSchema),
    defaultValues: {
      firstName: "",
      surname: "",
      email: "",
      phone: "",
      referralCode: "",
    },
    mode: "onBlur", // Enable real-time validation
  });

  const onSubmit = (data: AccountFormData) => {
    setIsSubmitting(true);
    console.log("Form submitted:", data);
    // Here you would typically call an API to create a user account
    // Then navigate to the next screen (email verification, etc.)

    // Simulate API call
    setTimeout(() => {
      setIsSubmitting(false);
      router.push({
        pathname: "/verifyOtp",
        params: { email: data.email },
      });
    }, 1500);
  };

  const handleCountryCodePress = () => {
    // In a real app, this would open a country selector modal
    console.log("Country code selection pressed");
  };

  const goToSignIn = () => {
    router.push("/signin");
  };

  // Fixed top padding of 96px as per Figma
  const topPadding = 96;

  return (
    <ScrollView
      className="flex-1 bg-white px-5"
      contentContainerStyle={{
        paddingTop: topPadding,
        paddingBottom: insets.bottom + 20,
        minHeight: height - insets.top - insets.bottom, // Ensure full height for bottom content
      }}
    >
      <View className="mb-8">
        <Text className="text-3xl font-poppins-semibold text-meristem-grey-900 mb-2">
          Create Your Account
        </Text>
      </View>

      <FormInput
        control={control}
        name="firstName"
        label="First name (As it appears on your ID)"
        placeholder="Enter your first name"
        error={errors.firstName}
        autoCorrect={false}
        disabled={isSubmitting}
      />

      <FormInput
        control={control}
        name="surname"
        label="Surname"
        placeholder="Enter your surname"
        error={errors.surname}
        autoCorrect={false}
        disabled={isSubmitting}
      />

      <FormInput
        control={control}
        name="email"
        label="Email address"
        placeholder="Enter your email address"
        error={errors.email}
        keyboardType="email-address"
        autoCapitalize="none"
        autoCorrect={false}
        disabled={isSubmitting}
      />

      <PhoneInput
        control={control}
        name="phone"
        label="Phone number"
        placeholder="00 00000 000"
        error={errors.phone}
        countryCode={countryCode}
        onCountryPress={handleCountryCodePress}
        disabled={isSubmitting}
      />

      <FormInput
        control={control}
        name="referralCode"
        label="Referral code (Optional)"
        placeholder="Enter referral code"
        autoCapitalize="none"
        autoCorrect={false}
        disabled={isSubmitting}
      />

      <TouchableOpacity
        onPress={handleSubmit(onSubmit)}
        className={`rounded-full py-4 w-full mt-6 mb-4 ${
          !isValid || isSubmitting
            ? "bg-meristem-grey-200"
            : "bg-meristem-green"
        }`}
        disabled={!isValid || isSubmitting}
      >
        <Text className="text-white font-poppins-medium text-center text-base">
          {isSubmitting ? "Processing..." : "Continue"}
        </Text>
      </TouchableOpacity>

      {/* Spacer to push the "Already have an account" text to the bottom */}
      <View className="flex-1" />

      {/* "Already have an account" moved to bottom */}
      <View className="flex-row justify-center items-center py-6">
        <Text className="text-meristem-grey-500 font-poppins-regular">
          Already have an account?
        </Text>
        <TouchableOpacity onPress={goToSignIn} className="ml-1">
          <Text className="text-meristem-green font-poppins-medium">
            Sign in
          </Text>
        </TouchableOpacity>
      </View>
    </ScrollView>
  );
};

export default CreateAccountForm;
