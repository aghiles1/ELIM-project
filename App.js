import React from 'react';
import { StyleSheet, Text, View, Button} from 'react-native';

import Home from './components/Home'
import AddDiscoveryComponent from "./components/AddDiscoveryComponent";
import {createStackNavigator} from "react-navigation";
import LoginComponent from "./components/LoginComponent";
import MapComponent from "./components/MapComponent";
import SigninComponent from "./components/SigninComponent";

export default class App extends React.Component {

  constructor(props) {
    super(props);
  }

  render() {
    return (
      <RootStack/>
    );
  }
}

const RootStack = createStackNavigator({
    Login: {screen: LoginComponent},
    Home: { screen: Home },
    Discovery: {screen: AddDiscoveryComponent},
    SignIn: {screen: SigninComponent },
});
