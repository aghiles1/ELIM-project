import React from 'react';
import { StyleSheet, Text, View, Button} from 'react-native';

import Home from './components/Home'
import AddDiscoveryComponent from "./components/AddDiscoveryComponent";
import {createStackNavigator} from "react-navigation";

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
    Home: { screen: Home },
    Discovery: {screen: AddDiscoveryComponent}
});
