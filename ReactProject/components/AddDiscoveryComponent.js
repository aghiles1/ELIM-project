import React, {Component} from 'react';
import {StyleSheet, Text, View, TouchableOpacity} from 'react-native';
import MultiSelect from 'react-native-multiple-select';
const IPAdress = require("../utils/ipAdress");

export default class AddDiscoveryComponent extends Component {

    constructor(props) {
        super(props);
        this.state = {
            selectedMushroomType: [],
            availableMushroomTypes: [{id:"0", name: "Morille"}, {id:"1", name: "Bolet"}, {id:"2", name: "Cepe"}, {id:"3", name: "Chanterelle"}],
            userId: this.props.navigation.getParam("userId"),
            latitude: 0,
            longitude: 0
        }
    }

    onSelectedItemsChange(newType) {
        console.log("newType: ", newType);
        this.setState({selectedMushroomType: newType});
    }

    componentDidMount() {
        navigator.geolocation.getCurrentPosition(
            (position) => {
                const lat = parseFloat(position.coords.latitude);
                const long = parseFloat(position.coords.longitude);

                this.setState({
                    latitude: lat,
                    longitude: long
                })
            },
            (error) => {
                this.setState({error: error.message});
                alert(error.message)
            },
            {enableHighAccuracy: true, maximumAge: 0, distanceFilter: 1},
        );
    }

    shareDiscovery(){
        if(this.state.selectedMushroomType !== ""){
            console.log("id ", this.state.userId);
            console.log("qsdd ", this.state.availableMushroomTypes[this.state.selectedMushroomType[0]].name);
            let request = 'http:' + IPAdress.ipAdress + ':8080/ShroomGo/shroom/add?' + "type=" + this.state.availableMushroomTypes[this.state.selectedMushroomType[0]].name + "&userID="+ this.state.userId + "&longitude=" + this.state.longitude + "&latitude=" + this.state.latitude;
            console.log("request ", request);
            fetch(request, {
                method: 'POST',
                headers: {
                    Accept: 'text/plain;charset=UTF-8',
                    'Content-Type': 'text/plain;charset=UTF-8',
                },
                body:""
            }).then((response)=>{
                console.log("dsdasd ", response);
                Alert.alert("","Découverte partagée avec succès")
            })
            .catch((error) => {
                Alert.alert("","Une erreur est survenue, veuillez réessayer");
                console.error(error);
            });
        }
    }

    render() {
        return (
            <View style={styles.mainView}>
                <Text style={styles.texts}>Partager ma découverte</Text>
                <MultiSelect
                    style={styles.picker1}
                    single
                    items={this.state.availableMushroomTypes}
                    uniqueKey="id"
                    onSelectedItemsChange={this.onSelectedItemsChange.bind(this)}
                    selectedItems={this.state.selectedMushroomType}
                    selectText="Pick Items"
                    searchInputPlaceholderText="Search Items..."
                    tagRemoveIconColor="green"
                    tagBorderColor="green"
                    tagTextColor="green"
                    selectedItemTextColor="green"
                    selectedItemIconColor="green"
                    itemTextColor="green"
                    displayKey="name"
                    searchInputStyle={{ color: 'green' }}
                    itemStyle={{backgroundColor: 'rgba(255,255,255,0.2)'}}
                    submitButtonColor="green"
                    submitButtonText="Choisir"
                />
                <TouchableOpacity style={styles.but} onPress={this.shareDiscovery.bind(this)}>
                    <Text style={styles.textStyle}>Partager la découverte</Text>
                </TouchableOpacity>
            </View>
        )
    };
}

const styles = StyleSheet.create({
    texts: {
        marginBottom: 20,
        marginTop: 10
    },
    mainView: {
      backgroundColor: "#3498db",
      flex: 1,
      flexDirection: "column",
      paddingLeft: 10,
      paddingRight: 10
    },
    textStyle: {
        fontSize:15,
        fontWeight: "500",
        color: '#ffffff',
        textAlign: 'center'
    },
    but: {
        padding: 10,
        backgroundColor: '#2196F3',
        marginLeft: '20%',
        marginRight: "20%",
        borderRadius: 3,
        elevation: 5
    },
    picker1: {
        marginTop: 50,
        paddingTop: 50
    }
});
