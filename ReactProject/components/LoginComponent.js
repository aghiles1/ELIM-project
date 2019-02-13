import React, {Component} from 'react';
import {Button, StyleSheet, Text, Image, KeyboardAvoidingView, View, TextInput, TouchableOpacity} from 'react-native';
import logoImg from '../assets/shroomGo.png';
const IPAdress = require("../utils/ipAdress");

export default class LoginComponent extends Component {

    constructor(props){
        super(props);
        this.placeholderUser = "Nom d'utilisateur";
        this.placeholderPass = "Mot de passe";
        this.placeholderColor = "rgba(255,255,255,0.6)";
        this.state = {
            userName: ""
        }
    }

    async goToSign(event) {
        this.props.navigation.navigate("SignIn");
    }
    async goToHome(event) {
        console.log("userName: ", this.state.userName);
        if(this.state.userName !== "") {
            let request = 'http://' + IPAdress.ipAdress + ':8080/ShroomGo/shroom/connexion?' + "userName="+ this.state.userName;
            console.log("request ", request);
            fetch(request, {
                method: 'GET'
            }).then((response)=>{
                return response.json()
            })
            .then((resJson)=> {
                console.log(resJson);
                this.props.navigation.navigate("Home", {userId: resJson});
            })
            .catch((error) => {
                Alert.alert("","Une erreur est survenue, veuillez réessayer");
                console.log("sdsqsdsqd");
                console.error(error);
            });
        }
    }


    render() {
        return (
            <KeyboardAvoidingView behavior="padding" style={styles.container}>
                <View style={styles.logoContainer}>
                    <Image source={logoImg} style={styles.image} />
                    <Text style={styles.title}>Bienvenue sur l'application ShroomGo</Text>
                </View>
                <TextInput
                    placeholder={this.placeholderUser}
                    onChangeText={(text) => this.setState({userName: text})}
                    style={styles.input}
                    returnKeyLabel="suivant"
                    placeholderTextColor={this.placeholderColor}
                    onSubmitEditing={() => this.motdepasse.focus() }
                />
                <TextInput
                    placeholder={this.placeholderPass}
                    style={styles.input}
                    secureTextEntry
                    returnKeyLabel="valider"
                    placeholderTextColor={this.placeholderColor}
                    ref={ (input) => this.motdepasse = input }

                />
                <TouchableOpacity style={styles.validContainer}
                                  onPress={this.goToHome.bind(this)}
                >
                    <Text style={styles.validate}>Connexion</Text>
                </TouchableOpacity>
                <TouchableOpacity
                    onPress={this.goToSign.bind(this)}
                    style={styles.validContainer}
                >
                    <Text style={styles.validate}>Inscription</Text>
                </TouchableOpacity>
            </KeyboardAvoidingView>
        )
    }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#3498db',
        padding: 25,
    },
    image: {
        width: 100,
        height: 100,
    },
    logoContainer: {
        alignItems: 'center',
        flexGrow: 1,
        justifyContent: 'center',
    },
    title: {
        color: 'white',
        marginTop: 20,
    },
    validContainer: {
        backgroundColor: '#2988bf',
        paddingVertical: 10,
        marginVertical: 10,
    },
    validate: {
        textAlign: 'center',
        color: 'white',
    },
    input: {
        marginBottom: 10,
        height: 40,
        color: '#fff',
        backgroundColor: 'rgba(255,255,255,0.2)'
    }
});
