import React, {Component} from 'react';
import {StyleSheet, Text, View} from 'react-native';

export default class MapComponent extends Component {

    constructor(props){
        super(props);
        this.state = {
            positions: ""
        }
    }

    componentDidMount(){
        let request = 'http://172.19.250.16:8080/ShroomGo/shroom/positions';
        fetch(request, {
            method: 'GET'
        })
            .then((response) => response.text())
            .then((responseText) => {
                console.log(responseText);
                this.setState({
                    positions: responseText
                })
            })
            .catch((error) => {
                console.error(error);
            });
    }

    render() {
        return (
            <View>
                <Text>{this.state.positions}</Text>
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