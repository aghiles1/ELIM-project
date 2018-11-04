import React, {Component} from 'react';
import {Button, StyleSheet, Text, View} from 'react-native';

class Home extends Component {

    constructor(props){
        super(props);

    }

    async goToAddDiscovery(event) {
        this.props.navigation.navigate("Discovery");
    }

    render() {
        return (
            <View style={styles.container}>
                <Text>This is the first shroomGo page!</Text>
                <Button
                    onPress={this.goToAddDiscovery.bind(this)}
                    title="Ajouter une découverte"
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