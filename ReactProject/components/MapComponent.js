import React, {Component} from 'react';
import {StyleSheet, Text, View} from 'react-native';

export default class MapComponent extends Component {

    constructor(props){
        super(props);
        this.state = {
            positions: "",
            text:""
        }
    }

    componentDidMount(){
        let request = 'http://172.19.250.16:8080/ShroomGo/shroom/positions';
        fetch(request, {
            method: 'GET'
        })
            .then((response) => response.text())
            .then((responseText) => {
                this.setState({
                    positions: responseText
                });
            })
            .catch((error) => {
                console.error(error);
            });
    }

    getListPosition(text) {
        let tab = text.split("\n");
        let str = "";
        for (let i = 0; i < tab.length - 1; i++) {
            str += tab[i].split("-")[2] + " a trouvé le chamgnon " + tab[i].split("-")[0] + " à la position (lat: " + tab[i].split("-")[1].split(",")[1] + " lng: " + tab[i].split("-")[1].split(",")[0] +")\n";
        }
        return str;
    }

    render() {
        return (
            <View>
                <Text>{this.state.positions !== "" ? this.getListPosition(this.state.positions) : ""}</Text>
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