const myApp = {

    // Préparation des données
    data() {
        console.log("data");
        return {
            axios: null,
            listUsers : [],
            currentUser : null,
            editable : null,
            errors: [],
            added :null,
            token : null,
            listActivities : [],
            listCurrentActivities : [],

        }
    },

    // Mise en place de l'application
    mounted() {
        this.token = this.getCookie('access_token');
        //this.token = localStorage.getItem("token");
        console.log("Mounted ");
        if(this.token != null && this.token !== ""){
            this.axios = axios.create({
                baseURL: 'http://localhost:8081/',
                timeout: 1000,
                headers: { 'Content-Type': 'application/json', 'Authorization': 'Bearer ' +  this.token},

            });
        }else {
            this.token = "";
            this.axios = axios.create({
                baseURL: 'http://localhost:8081/',
                timeout: 1000,
                headers: { 'Content-Type': 'application/json'}
            });
        }


        this.refresh();
    },

    methods: {
        // Place pour les futures méthodes

        deleteUser: function (id){
            this.axios.delete('/users/' + id).then(this.refresh);

        },
        editUser: function (id){
            this.axios.get("/users/"+id).then(r => this.editable = r.data);
        },
        viewUser: function (id){
            this.axios.get('/users/' + id).then(r =>{
                this.currentUser = r.data;
                this.listCurrentActivities = this.currentUser.cv;
            })
        },
        refresh: function (){
            this.axios.get("/users").then(r => {

                this.listUsers = r.data;
            });
            this.axios.get("/activities").then(r => {
                this.listActivities = r.data;
            });
            console.log(this.token)

        },
        validateEmail(email) {
            const re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
            return re.test(email);
        },
        isValidHttpUrl(string) {
            let url;

            try {
                url = new URL(string);
            } catch (_) {
                return false;
            }

            return url.protocol === "http:" || url.protocol === "https:";
        },
        checkPassword(str)
        {
            // at least one number, one lowercase and one uppercase letter
            // at least six characters that are letters, numbers or the underscore
            var re = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])\w{8,}$/;
            return re.test(str);
        },
        submitUser: function (id){
            if(this.editable.firstname === ""){
                this.errors.firstname= "écrire son prenom";
            }if(this.editable.lastname === ""){
                this.errors.lastname = "rempir votre nom de famille";
            }
            if(!this.validateEmail(this.editable.email)){
                this.errors.email = "email non valide";
            }
            if(this.editable.birthday === null){
                this.errors.birthday = "remplir la date de naissance";
            }
            if(!this.isValidHttpUrl(this.editable.website)){
                this.errors.website = "entrer un site";
            }
            if(!this.checkPassword(this.editable.password)){
                this.errors.password = "Le mot de passe doit etre supérieur à 8 caracteres et doitcontenir au moins un chiffre,une majuscule, une majuscule ";
            }
            else {
                if (this.added != null) {
                    this.editable.token = "";
                    this.axios.post("/users/signup", this.editable,{params:{
                            email: this.editable.email, password: this.editable.password
                        }})
                        .then(() =>{
                            this.editable.token = "";
                            this.refresh()
                        this.editable = null;
                        this.added = null;
                    });
                } else {
                    this.axios.put("/users/" + id, this.editable).then(() => {
                        this.refresh();
                        this.editable = null;
                        this.errors.firstname = "";
                        this.errors.lastname = "";
                        this.errors.birthday = "";
                        this.errors.password = "";
                        this.errors.email = "";
                        this.errors.website = "";

                    });
                }
            }
        },
        addUser : function (){
            this.editable = {email : "",password: "",firstname: "", lastname: "", website: "",birthday: null,token: ""}
            this.added = true;
        },
        logout: function (){

            const AuthStr = 'Bearer '.concat(this.token);
            axios.get("/users/logout", { headers: { Authorization: AuthStr } })
                .then(response => {
                    // If request is good...
                    console.log(response.data);
                    this.token="";
                    document.cookie="access_token=" + "";
                    this.refresh()
                })
                .catch((error) => {
                    this.token="";
                    document.cookie="access_token=" + "";
                    this.refresh()
                });
        },
         getCookie : function (cName){
             const name = cName + "=";
             const cDecoded = decodeURIComponent(document.cookie); //to be careful
             const cArr = cDecoded.split('; ');

             let res = null;
             cArr.forEach(val => {
                 if (val.indexOf(name) === 0) res = val.substring(name.length);
             })
             return res
        }
    }



}

Vue.createApp(myApp).mount('#myApp');
Vue.createApp(myApp).mount('#app');