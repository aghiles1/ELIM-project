import React, {Component} from 'react';
import {Button, StyleSheet, Text, View} from 'react-native';

class Home extends Component {

    constructor(props){
        super(props);
        this.state = {
            userName: this.props.navigation.getParam("userName")
        }

    }

    async goToAddDiscovery(event) {
        this.props.navigation.navigate("Discovery", {userName: this.state.userName});
    }

    async goToDiscovery(event) {
        this.props.navigation.navigate("Map");
    }

    render() {
        return (
            <View style={styles.container}>
                <Text>This is the first shroomGo page!</Text>
                <Button
                    onPress={this.goToAddDiscovery.bind(this)}
                    title="Ajouter une découverte"
                />
                <Button
                    onPress={this.goToDiscovery.bind(this)}
                    title="Découvertes"
                />

            </View>
        )
    }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#fff',
        alignItems: 'center',
        justifyContent: 'center',
    },
});

export default Home;