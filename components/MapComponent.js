import React, {Component} from 'react';
import { Button, StyleSheet, Text, Image, KeyboardAvoidingView , View} from 'react-native';

export default class MapComponent extends Component {

    constructor(props){
        super(props);

    }

    getPositions() {

        let request = 'http://192.168.1.10:8080/ShroomGo/shroom/positions';
        fetch(request, {
            method: 'GET',
            headers: {
                Accept: 'text/plain;charset=UTF-8',
                'Content-Type': 'text/plain;charset=UTF-8',
            },
            body:""
        }).then((response)=>{console.log(response.text())})
            .catch((error) => {
                console.error(error);

            });
    }

    render() {
        return (
            <View>
                <Text>{this.getPositions()}</Text>
            </View>
        )
    }
}

const styles = StyleSheet.create({
    map: {
        left: 0,
        right: 0,
        top: 0,
        bottom: 0,
        position: 'absolute'
    },
    container: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center'

    }
});