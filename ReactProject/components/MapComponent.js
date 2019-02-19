import React, {Component} from 'react';
import {StyleSheet, Text, View, Dimensions, ScrollView, Picker, Button, Alert} from 'react-native';
import MapView, {PROVIDER_GOOGLE} from 'react-native-maps';
import MapCircle from "react-native-maps/lib/components/MapCircle";
import MultiSelect from 'react-native-multiple-select';

const IPAdress = require("../utils/ipAdress");
const randomColor = require('randomcolor');
const rgba = require('rgba-convert');
const {width, height} = Dimensions.get('window');
const ASPEC_RATIO = width / height;
const LATITUDE_DELTA = 0.05; // Zoom initial
const LONGITUDE_DELTA = LATITUDE_DELTA * ASPEC_RATIO;// Zoom initial

export default class MapComponent extends Component {

    constructor(props){
        super(props);

        this.state = {
            positions: "",
            text:"",
            colors: new Map(),
            allChecked: true,
            initialPosition : {
                latitude: 0,
                longitude: 0,
                latitudeDelta: LATITUDE_DELTA,
                longitudeDelta: LONGITUDE_DELTA
            },
            userId: this.props.navigation.getParam("userId"),
            items: [],
            zoneRadius: 1,
            selectedMushroomTypes: []
        };
        this.updateZoneRadius = this.updateZoneRadius.bind(this);
        this.updateMushroomType = this.updateMushroomType.bind(this);
        this.setColors = this.setColors.bind(this);
    }

    componentDidMount(){
        navigator.geolocation.getCurrentPosition(
            (position) => {
                const lat = parseFloat(position.coords.latitude);
                const long = parseFloat(position.coords.longitude);

                const initialRegion = {
                    latitude: lat,
                    longitude: long,
                    longitudeDelta: LONGITUDE_DELTA,
                    latitudeDelta: LATITUDE_DELTA,
                    error: null,
                };
                this.setPosition(initialRegion);
            },
            (error) => {this.setState({error: error.message}); alert(error.message)},
            {enableHighAccuracy: true, maximumAge: 0, distanceFilter: 1},
        );
        let request = 'http://' + IPAdress.ipAdress +':8080/ShroomGo/shroom/positions';
        fetch(request, {
            method: 'GET'
        })
            .then((response) => response.json())
            .then((responseJson) => {
                console.log(responseJson);
                let colors = this.setColors(responseJson);
                this.setState({
                    items: this.getTypesItems(colors),
                    selectedMushroomTypes: [(colors.size - 1).toString()]
                });
            })
            .catch((error) => {
                console.error(error);
            });
    }

    setPosition(region) {
        this.setState({
            initialPosition: region
        });
    }

    updateZoneRadius(zoneRadius){
        this.setState({
            zoneRadius: zoneRadius
        })
    }

    getArray(){
        let result = [];
        for(let i = 0; i < 20; i++){
            result[i] = i+1;
        }
        return result;
    }

    updateMushroomType(newTypes) {
        this.setState({selectedMushroomTypes: newTypes});
    }


    getZones(){
        let request = 'http://' + IPAdress.ipAdress +':8080/ShroomGo/shroom/position?'+"centerLat="+this.state.initialPosition.latitude+"&centerLong="+this.state.initialPosition.longitude+"&size="+this.state.zoneRadius + "&userID="+this.state.userId + this.generateShroomTypesArray();
        console.log("request ", request);
        fetch(request, {
            method: 'GET'
        })
            .then((response) => response.json())
            .then((responseJson) => {
                console.log(responseJson);
                let colors = this.setColors(responseJson);
                this.setState({
                    positions: responseJson,
                    colors: colors,
                    items: this.getTypesItems(colors)
                });
            })
            .catch((error) => {
                console.error(error);
            });
    }

    generateShroomTypesArray() {
        if(this.state.selectedMushroomTypes.length === 0 || (this.state.selectedMushroomTypes.length === 1 & this.state.items[0].name === "tous les champignons")) {
            return "&shroomTypes=all"
        }
        else {
            let result = "";
            for(let shroomType in this.state.selectedMushroomTypes) {
                console.log("shroomType " + shroomType);
                result += "&array=" + this.formatName(this.state.items[shroomType].name);
            }
            return result;
        }
    }

    formatName(name) {
        return name[0].toUpperCase() + name.substring(1, name.length);
    }

    setColors(discoveries){
        let colors = this.state.colors;
        for(let discovery of discoveries) {
            let mushroomType = discovery.type.toLowerCase();
            if(!colors.has(mushroomType)){
                let lightColor = randomColor({
                    luminosity: "light",
                    format: "rgba",
                    alpha: "0.5"
                });
                let darkColor = randomColor({
                    luminosity: "dark",
                    hue: rgba.hex(lightColor)
                });
                colors.set(mushroomType, {lightColor: lightColor, darkColor: darkColor, isChecked: false});
            }
        }
        colors.set("tous les champignons", {lightColor: "lightgrey", darkColor: "black", isChecked: true});
        return colors;
    }

    getTypesItems(colors) {
        let items = Array.from(colors.keys());
        let result = [];
        let id = 0;
        for(let item of items){
            let type = {
              id: id.toString(),
              name: item
            };
            result.push(type);
            id++;
        }
        return result
    }

    changeRegion(event){
        let newPosition = event.nativeEvent.coordinate;
        this.setState({
            initialPosition: {
                latitude: newPosition.latitude,
                longitude: newPosition.longitude,
                latitudeDelta: LATITUDE_DELTA,
                longitudeDelta: LONGITUDE_DELTA
            }
        })
    }

    render() {
        let pickerItems = this.getArray().map((index)=>{
            return <Picker.Item key={index} value={index} label={index.toString()} />
        });
        return (
            <View style={styles.container}>
                <View style={styles.mapContainer}>
                    <MapView
                        style={styles.map}
                        region={this.state.initialPosition}
                        onPress={(event) => this.changeRegion(event)}>
                        {this.state.positions !== "" ? (this.state.positions.map((pos, index) =>
                            <MapCircle key={index} center={{latitude: pos.position.latitude, longitude: pos.position.longitude}} radius={pos.degradation ? pos.degradation * 1000 : 500} strokeColor={this.state.colors.get(pos.type.toLowerCase()).darkColor} strokeWidth={1} fillColor={this.state.colors.get(pos.type.toLowerCase()).lightColor} zIndex={4}/>
                        )): null}
                        <MapView.Marker
                            title="Position actuelle"
                            coordinate={this.state.initialPosition}>
                        </MapView.Marker>
                    </MapView>
                </View>
                <ScrollView style={styles.legendView}>
                    <View style={styles.pickerView}>
                        <Text style={styles.pickerLabels}>Chercher dans un rayon de </Text>
                        <Picker style={styles.picker} selectedValue = {this.state.zoneRadius} onValueChange = {this.updateZoneRadius}>
                            {pickerItems}
                        </Picker>
                        <Text>km</Text>
                    </View>
                    <View style={{flex: 1}}>
                        <MultiSelect style={styles.picker1}
                            items={this.state.items}
                            uniqueKey="id"
                            onSelectedItemsChange={this.updateMushroomType}
                            selectedItems={this.state.selectedMushroomTypes}
                            selectText="Types de champignons à visualiser"
                            searchInputPlaceholderText="Choisissez un type"
                            tagRemoveIconColor="red"
                            tagBorderColor="#CCC"
                            tagTextColor="#CCC"
                            selectedItemTextColor="#CCC"
                            selectedItemIconColor="green"
                            itemTextColor="#000"
                            displayKey="name"
                            searchInputStyle={{ color: 'black' }}
                            submitButtonColor="green"
                            submitButtonText="Choisir"
                        />
                    </View>
                    <Button
                        onPress={()=>this.getZones()}
                        title="Voir les découvertes"
                        color="#841584"
                    />
                </ScrollView>
            </View>
        )

    }
}

const styles = StyleSheet.create({
    icons: {
        flex: 1
    },
    labelsView: {
      flexDirection: "row",
      flex: 4
    },
    pickerView: {
        flex: 3,
        flexDirection: "row",
        alignItems: "center",
        justifyContent: 'center',
    },
    pickerLabels: {
      flex: 1
    },
    picker: {
        flex: 0.5,
        width: "25%"
    },
    pickerLabels1: {
        flex: 1.5
    },
    picker1: {
        flex: 2.5,
        width: "100%",
        marginLeft: 10
    },
    legendView: {
        marginLeft: '5%',
        marginRight: '5%',
    },
    mapContainer: {
        width: '90%',
        height: '50%',
        marginLeft: '5%',
        marginRight: '5%',
        marginTop: '10%',
        borderStyle: 'solid',
        borderRadius: 3,
        overflow: 'hidden'
    },
    map: {
      width: '100%',
      height: '100%'
    },
    container: {
        flex: 1,
        backgroundColor: 'white'
    }
});
