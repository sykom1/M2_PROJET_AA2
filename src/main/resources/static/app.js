const myApp = {

    // Préparation des données
    data() {
        console.log("data");
        return {
            counter: 1,
            message: "Hello",
            list: [10, 20, 30],
            axios: null,
            listUsers : [],
            currentUser : null,
            editable : null,
            errors: [],
            added :null
        }
    },

    // Mise en place de l'application
    mounted() {

        console.log("Mounted ");
        this.axios = axios.create({
            baseURL: 'http://localhost:8081/',
            timeout: 1000,
            headers: { 'Content-Type': 'application/json' },
        });
        this.refresh();
    },

    methods: {
        // Place pour les futures méthodes
        incCounter: function(step) {

            console.log("incremente le compteur ");
            this.counter+=step;
            this.axios.get('/users/1')
                .then(r => {
                    console.log("read user 1 done");
                    this.message = r.data;
                });
        },
        deleteUser: function (id){
            this.axios.delete('/users/' + id).then(this.refresh);

        },
        editUser: function (id){
            this.axios.get("/users/"+id).then(r => this.editable = r.data);
        },
        viewUser: function (id){
            console.log("salut")
            this.axios.get('/users/' + id).then(r =>{
                this.currentUser = r.data;

            })
        },
        refresh: function (){
            this.axios.get("/users").then(r => {
                this.listUsers = r.data;
            });
        },
        populateUsers: function (){

            for(let i = this.listUsers.length; i < this.listUsers.length+3;i++) {
                const user = {
                    firstname: "user" + i, lastname: "lastname" + i,
                    email: "email" + i + "@gmail.com", password:"aaa",
                    website:"http://site" + i + ".com",birthday: new Date(2018,8,12),
                    token: null
                    }
                    console.log(user);
                this.axios.post("/users/signup", user,{params:{
                    email: user.email, password: user.password
                    }})
                    .then(r =>{
                    user.token = r.data;
                    this.refresh()

                });


            }
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
                    console.log(this.editable);
                    console.log(this.added);
                    this.editable.token = "";
                    this.axios.post("/users/signup", this.editable,{params:{
                            email: this.editable.email, password: this.editable.password
                        }})
                        .then(r =>{
                            this.editable.token = r.data;
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
        subUser: function (name,year,desc){

        }

    },



}

Vue.createApp(myApp).mount('#myApp');
Vue.createApp(myApp).mount('#app');