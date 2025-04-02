import { useEffect } from "react";
import { Redirect, useRouter } from "expo-router";
import SplashScreen from "../components/specific/SplashScreen";

export default function Index() {
  const router = useRouter();

  return <Redirect href={"/onboarding"} />;

  // useEffect(() => {
  //   // Simulate a delay before navigating to onboarding
  //   const timer = setTimeout(() => {
  //     router.replace("/onboarding");
  //     // router.replace("/createAccount");
  //   }, 2000);

  //   return () => clearTimeout(timer);
  // }, []);

  // return <SplashScreen />;
}
