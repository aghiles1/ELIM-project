import React, {Component} from 'react';
import {Button, StyleSheet, Text, Image, KeyboardAvoidingView, View, TextInput, TouchableOpacity} from 'react-native';
import logoImg from '../assets/shroomGo.png';

export default class SigninComponent extends Component {

    constructor(props){
        super(props);
        this.placeholderUser = "Nom d'utilisateur";
        this.placeholderPass = "Mot de passe";
        this.placeholderEmail = "Nom d'utilisateur";
        this.placeholderConfPass = "Confirmation du mot de passe";
        this.placeholderColor = "rgba(255,255,255,0.6)";
    }

    async goToLogin(event) {
        this.props.navigation.navigate("Login");
    }
    async goToHome(event) {
        this.props.navigation.navigate("Home");

    }

    render() {
        return (
            <KeyboardAvoidingView behavior="padding" style={styles.container}>
                <View style={styles.logoContainer}>
                    <Image source={logoImg} style={styles.image} />
                    <Text style={styles.title}>Bienvenu sur l'application ShroomGo</Text>
                </View>
                <TextInput
                    placeholder={this.placeholderUser}
                    style={styles.input}
                    returnKeyLabel="suivant"
                    placeholderTextColor={this.placeholderColor}
                    onSubmitEditing={() => this.motdepasse.focus() }
                />
                <TextInput
                    placeholder={this.placeholderEmail}
                    style={styles.input}
                    email-address
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
                <TextInput
                    placeholder={this.placeholderConfPass}
                    style={styles.input}
                    secureTextEntry
                    returnKeyLabel="valider"
                    placeholderTextColor={this.placeholderColor}
                    ref={ (input) => this.motdepasse = input }
                />
                <TouchableOpacity style={styles.validContainer}
                                  onPress={this.goToHome.bind(this)}
                >
                    <Text style={styles.validate}>Enregistrer</Text>
                </TouchableOpacity>
                <TouchableOpacity
                    style={styles.validContainer}
                    onPress={this.goToLogin.bind(this)}
                >
                    <Text style={styles.validate}>Se Connecter</Text>
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
    validContainer: {
        backgroundColor: '#2988bf',
        paddingVertical: 10,
        marginVertical: 10,
    },
    validate: {
        textAlign: 'center',
        color: 'white',
    },
    title: {
        color: 'white',
        marginTop: 20,
    },
    input: {
        marginBottom: 10,
        height: 40,
        color: '#fff',
        backgroundColor: 'rgba(255,255,255,0.2)'
    },
});