import React, {Component} from 'react';
import { TouchableOpacity, StyleSheet, View, Text, TextInput } from 'react-native';

class AddDiscoveryComponent extends Component {

    constructor(props){
        super(props);
        this.state = {
            latitude: 0,
            longitude: 0,
            mushroomType: ""
        };
        this.placeholder = "Entrez le type de champignon que vous avez trouvé";
    }

    componentDidMount() {
        navigator.geolocation.getCurrentPosition(
            (position) => {

                const lat = parseFloat(position.coords.latitude);
                const long = parseFloat(position.coords.longitude);

                this.setState({
                    latitude: lat,
                    longitude: long
                });
                console.log(this.state.latitude);
                console.log(this.state.longitude);
            },
            (error) => console.log(error),
            {enableHighAccuracy: true, maximumAge: 0, distanceFilter: 1},
        );
    }

    async shareDiscovery(){
        if(this.state.mushroomType !== ""){

            let request = 'http://192.168.1.10:8080/ShroomGo/shroom/add?' + "type=" + this.state.mushroomType + "&userID=popol&longitude=" + this.state.longitude + "&latitude=" + this.state.latitude;
            fetch(request, {
                method: 'POST',
                headers: {
                    Accept: 'text/plain;charset=UTF-8',
                    'Content-Type': 'text/plain;charset=UTF-8',
                },
                body:""
            }).then((response)=>{
                console.log(response.text())
            })
              .catch((error) => {
                  console.log("sdsqsdsqd");
                  console.error(error);
            });
        }
    }

    render() {
        return (
            <View behavior="padding" enabled>
                <Text style={styles.texts}>Vos coordonnées actuelles :</Text>
                <Text style={styles.texts}>Latitude : {this.state.latitude}, Longitude : {this.state.longitude}</Text>
                <TextInput style={styles.textInput} onChangeText={(text) => this.setState({mushroomType: text})} placeholder={this.placeholder}/>
                <TouchableOpacity style={styles.but} onPress={this.shareDiscovery.bind(this)}>
                    <Text style={styles.textStyle}>Partager la découverte</Text>
                </TouchableOpacity>
            </View>
        )
    }
}
export default AddDiscoveryComponent;
const styles = StyleSheet.create({
    textInput: {
        height: 40,
        marginTop: 10,
        marginLeft: 10,
        marginRight: 10
    },
    texts: {
        marginTop: 10,
        marginLeft: 10
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
    }
});