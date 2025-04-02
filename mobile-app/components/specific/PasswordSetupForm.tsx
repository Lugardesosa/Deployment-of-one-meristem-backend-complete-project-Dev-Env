import React, { useState, useEffect } from "react";
import {
  View,
  Text,
  TouchableOpacity,
  StyleSheet,
  ActivityIndicator,
  ScrollView,
} from "react-native";
import { useRouter } from "expo-router";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import { useSafeAreaInsets } from "react-native-safe-area-context";
import PasswordInput from "../common/PasswordInput";
import PasswordRequirement from "../common/PasswordRequirement";
import SuccessScreen from "../common/SuccessScreen";
import Animated, { FadeIn, FadeOut } from "react-native-reanimated";

// Password validation schema
const passwordSchema = z
  .object({
    password: z.string().min(1, "Password is required"),
    confirmPassword: z.string().min(1, "Please confirm your password"),
  })
  .refine((data) => data.password === data.confirmPassword, {
    message: "Passwords don't match",
    path: ["confirmPassword"],
  });

type FormData = z.infer<typeof passwordSchema>;

const PasswordSetupForm = () => {
  const router = useRouter();
  const insets = useSafeAreaInsets();
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [showSuccess, setShowSuccess] = useState(false);

  // Password requirements state
  const [requirements, setRequirements] = useState({
    length: false,
    uppercase: false,
    number: false,
    special: false,
  });

  // Count number of requirements met
  const requirementsMet = Object.values(requirements).filter(Boolean).length;
  const allRequirementsMet = requirementsMet === 4;

  const {
    control,
    handleSubmit,
    watch,
    formState: { errors },
    setError,
  } = useForm<FormData>({
    resolver: zodResolver(passwordSchema),
    defaultValues: {
      password: "",
      confirmPassword: "",
    },
  });

  // Watch password for requirements validation
  const password = watch("password");

  // Check password requirements on change
  useEffect(() => {
    if (password) {
      setRequirements({
        length: password.length >= 8 && password.length <= 20,
        uppercase: /[A-Z]/.test(password),
        number: /[0-9]/.test(password),
        special: /[!@#$%^&*(),.?":{}|<>]/.test(password),
      });
    } else {
      setRequirements({
        length: false,
        uppercase: false,
        number: false,
        special: false,
      });
    }
  }, [password]);

  const onSubmit = async (data: FormData) => {
    // Check if all requirements are met
    if (!allRequirementsMet) {
      setError("password", {
        message: "Password must meet all requirements",
      });
      return;
    }

    // Start submission
    setIsSubmitting(true);

    try {
      // Simulate API call
      await new Promise((resolve) => setTimeout(resolve, 1500));
      setShowSuccess(true);
    } catch (error) {
      console.error("Error setting password:", error);
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleSuccessButtonPress = () => {
    router.push("/signin");
  };

  if (showSuccess) {
    return (
      <SuccessScreen
        title="Congrats! 🚀"
        description="Your password has been successfully created. You're all set!"
        buttonText="Go to Sign In"
        onButtonPress={handleSuccessButtonPress}
      />
    );
  }

  // Content padding based on SafeArea
  const topPadding = Math.max(insets.top, 20);

  return (
    <Animated.ScrollView
      entering={FadeIn}
      exiting={FadeOut}
      style={styles.container}
      contentContainerStyle={{ paddingTop: topPadding + 24 }}
    >
      <View style={styles.header}>
        <Text style={styles.title}>Set Your Password</Text>
        <Text style={styles.subtitle}>
          To keep your account safe, we advice you create a strong password.
        </Text>
      </View>

      <PasswordInput
        control={control}
        name="password"
        label="Password"
        placeholder="Enter your password"
        error={errors.password}
        disabled={isSubmitting}
      />

      <PasswordInput
        control={control}
        name="confirmPassword"
        label="Confirm Password"
        placeholder="Re-enter password"
        error={errors.confirmPassword}
        disabled={isSubmitting}
      />

      <View style={styles.requirementsContainer}>
        <Text style={styles.requirementsTitle}>Your password must contain</Text>

        <PasswordRequirement
          label="Between 8-20 characters"
          isMet={requirements.length}
        />

        <PasswordRequirement
          label="1 upper case letter"
          isMet={requirements.uppercase}
        />

        <PasswordRequirement
          label="1 or more numbers"
          isMet={requirements.number}
        />

        <PasswordRequirement
          label="1 or more special characters"
          isMet={requirements.special}
        />
      </View>

      <TouchableOpacity
        style={[
          styles.createButton,
          (!allRequirementsMet || isSubmitting) && styles.createButtonDisabled,
        ]}
        onPress={handleSubmit(onSubmit)}
        disabled={!allRequirementsMet || isSubmitting}
      >
        {isSubmitting ? (
          <ActivityIndicator color="white" size="small" />
        ) : (
          <Text style={styles.createButtonText}>Create Password</Text>
        )}
      </TouchableOpacity>
    </Animated.ScrollView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "white",
    paddingHorizontal: 20,
  },
  header: {
    marginBottom: 32,
  },
  title: {
    fontSize: 32,
    fontFamily: "Poppins-SemiBold",
    color: "#333",
    marginBottom: 8,
  },
  subtitle: {
    fontSize: 16,
    fontFamily: "Poppins-Regular",
    color: "#868D96", // meristem-grey-300
    lineHeight: 24,
  },
  requirementsContainer: {
    marginTop: 16,
    marginBottom: 32,
  },
  requirementsTitle: {
    fontSize: 16,
    fontFamily: "Poppins-Medium",
    color: "#4B5563", // meristem-grey-500
    marginBottom: 16,
  },
  createButton: {
    backgroundColor: "#00643C", // meristem-green
    borderRadius: 50,
    paddingVertical: 16,
    alignItems: "center",
    justifyContent: "center",
    marginBottom: 24,
  },
  createButtonDisabled: {
    backgroundColor: "#ACB1B7", // meristem-grey-200
  },
  createButtonText: {
    color: "white",
    fontFamily: "Poppins-Medium",
    fontSize: 16,
  },
});

export default PasswordSetupForm;
