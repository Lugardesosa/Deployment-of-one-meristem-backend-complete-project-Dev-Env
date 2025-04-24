import React from "react";
import { View, Text, ScrollView } from "react-native";
import { Stack } from "expo-router";
import { StatusBar } from "expo-status-bar";
import { SafeAreaView } from "react-native-safe-area-context";
import VerificationBanner from "../../components/specific/VerificationBanner";
import Animated, { FadeInDown } from "react-native-reanimated";

const DashboardCard = ({
  title,
  value,
  subtitle,
}: {
  title: string;
  value: string;
  subtitle: string;
}) => (
  <Animated.View
    entering={FadeInDown.delay(400).springify()}
    className="bg-white p-4 rounded-2xl shadow-sm mb-4"
  >
    <Text className="text-meristem-grey-500 font-poppins-regular text-sm mb-1">
      {title}
    </Text>
    <Text className="text-meristem-grey-900 font-poppins-semibold text-2xl mb-1">
      {value}
    </Text>
    <Text className="text-meristem-grey-400 font-poppins-regular text-xs">
      {subtitle}
    </Text>
  </Animated.View>
);

export default function Dashboard() {
  return (
    <SafeAreaView className="flex-1 bg-neutral">
      <StatusBar style="dark" />
      <Stack.Screen
        options={{
          title: "Dashboard",
          headerStyle: { backgroundColor: "white" },
          headerShadowVisible: false,
          headerTitleStyle: {
            fontFamily: "Poppins-SemiBold",
          },
        }}
      />

      <ScrollView
        className="flex-1"
        contentContainerStyle={{
          flexGrow: 1,
          paddingBottom: 20,
        }}
      >
        <VerificationBanner />

        <View className="px-4">
          <Text className="text-2xl font-poppins-semibold text-meristem-grey-900 mb-4">
            Portfolio Overview
          </Text>

          <DashboardCard
            title="Total Portfolio Value"
            value="₦2,450,000.00"
            subtitle="Last updated: Today at 2:30 PM"
          />

          <DashboardCard
            title="Mutual Funds"
            value="₦1,200,000.00"
            subtitle="3 active funds"
          />

          <DashboardCard
            title="Fixed Income"
            value="₦850,000.00"
            subtitle="2 active investments"
          />

          <DashboardCard
            title="Equity Investments"
            value="₦400,000.00"
            subtitle="5 stocks in portfolio"
          />
        </View>
      </ScrollView>
    </SafeAreaView>
  );
}
