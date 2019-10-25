<template>
    <div>
        <nav class="navbar navbar-default">
            <div class="container-fluid" style="justify-content: start;">
                <input class="form-control" style="width:20%; margin-right: 10px" placeholder="id" v-if="isLogin !== true" v-model="user.id">
                <input class="form-control" style="width:20%; margin-right: 10px" placeholder="password" v-if="isLogin !== true" v-model="user.password">
                <button class="btn btn-primary" v-on:click="login">{{loginStatus}}</button>
            </div>
        </nav>

        <!-- main body of our application -->
        <div class="container" id="events">
            <!-- show the events -->
            <div class="col-sm-7">
                <!-- <button class="btn btn-primary" @click="order = order * -1">Reverse Order</button> -->
                <div>
                    <table>
                        <thead>
                            <tr>
                                <th style="width:80%;"><h5>Title</h5></th>
                                <th style="width:20%;"><h5>Operation</h5></th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr v-for="(notice, index) in notices" v-bind:key="notice.id">
                                <td class="table-hover" style="width:80%;" v-on:click="retrieveNotice(index)" ><h6>{{ notice.title }}</h6></td>
                                <td style="width:20%;"><button class="btn btn-xs btn-danger" v-on:click="deleteNotice(notice.id)">Delete</button></td>
                            </tr>
                        </tbody>
                    </table>
                 </div>
                <div>
                    <v-pagination v-model="currentPage"
                                :page-count="totalPages"
                                :classes="bootstrapPaginationClasses"
                                :labels="paginationAnchorTexts"></v-pagination>
                </div>

            </div>
            <!-- add an event form -->
            <div class="col-sm-5">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h3>Notice</h3>
                        <div>Writer : {{detailNotice.userName}}</div>
                        <div>created : {{detailNotice.createTime}}</div>
                        <div>last updated : {{detailNotice.lastUpdateime}}</div>
                        
                    </div>
                    <div class="panel-body">

                        <div class="form-group">
                            <input class="form-control" placeholder="Title" v-model="detailNotice.title">
                        </div>

                        <div class="form-group">
                            <textarea class="form-control" style="resize: none; height: 145px" placeholder="Description" v-model="detailNotice.contents"></textarea>
                        </div>

                        <button class="btn btn-primary" v-on:click="newNotice" v-if="isLogin === true">New</button>
                        <button class="btn btn-primary" v-on:click="registerNotice" v-if="isLogin === true">Submit</button>

                    </div>

                </div>
            </div>
        </div>
    </div>
</template>

<script>
import vPagination from './vue-plain-pagination.vue'
import axios from 'axios'

let interval = 5;
export default {
    components: { vPagination },
    data() {
        return {
            // Board
		    order: 1,
	        detailNotice: { id:'', title: '', contents: '', userName: '', createTime:'', lastUpdateime:'' },
            // events: [],
            // Pageing
            currentPageSize: 0,
            currentPage: 1,
            totalPages: 1,
            bootstrapPaginationClasses: {
                ul: 'pagination',
                li: 'page-item',
                liActive: 'active',
                liDisable: 'disabled',
                button: 'page-link'  
            },
            paginationAnchorTexts: {
                first: 'First',
                prev: 'Previous',
                next: 'Next',
                last: 'Last'
            },
            // Integration
            notices: [],
            totalCount: 0,
            // Login
            loginStatus: "Login",
            isLogin: false,
            user: { id: '', password: '', token: ''}
	    }
    },
    computed: {
    
    },
    methods: {

        fetchEvents: function() {

            let startIndex = (this.currentPage - 1) * interval;
            let lastIndex = this.currentPage * interval;

            this.events.splice(0, this.events.length);

            //console.log(">>> From : " + startIndex + " - To : " + lastIndex);

            // this.originalEvents.slice(startIndex, lastIndex).forEach(event => {
            //     this.events.push(event);
            // })
        },

		addEvent: function() {
			// if(this.event.name) {
            //     this.notices.push(this.event);
            //     this.event = { name: '', description: '', date: '' };
            //     this.totalCount += 1;
            //     this.totalPages = parseInt(this.totalCount / interval) + ((this.totalCount % interval) ? 1 : 0);

            //     this.fetchEvents();
			// }
		},
		
		deleteEvent: function(index) {
			// if (confirm('Are you sure you want to delete this event?')) {

            //     var targetIndex = index + (interval * (this.currentPage-1));
            //     console.log('Deleted Item >>> ' + targetIndex);
            //     this.notices.splice(targetIndex,1);
            //     this.totalCount -= 1;
            //     this.totalPages = parseInt(this.totalCount / interval) + ((this.totalCount % interval) ? 1 : 0);

            //     if (this.currentPage > this.totalPages) this.currentPage = this.totalPages;

            //     this.fetchEvents();
			// }
        },

        newNotice: function() {
            this.detailNotice = { id:'', title: '', contents: '', userName: '', createTime:'', lastUpdateime:'' };
        },

        insertNotice: function() {
            axios
                .post("http://localhost:7000/api/notice", {
                        "contents": this.detailNotice.title,
                        "title": this.detailNotice.contents,
                        "userName": this.user.id
                    },
                    {
                        "Content-Type": "application/json" ,
                        "X-AUTH-KEY": this.user.token,
                        "Access-Control-Allow-Origin": "*"
                    }
                )
                .then(response => {
                    console.log("공지 사항이 등록됐습니다.");
                    //this.detailNotice.id = response.data.returnCode;
                    this.getNotice(1);
                    this.newNotice();
                })
                .catch(response => {
                    console.log(response);
                    alert("공지사항 등록에 실패하였습니다.");

                })
                .finally(() => {
                    // always executed
                }
            );
        },

        updateNotice: function() {

            console.log(this.detailNotice.id);

            axios
                .put("http://localhost:7000/api/notice/"+this.detailNotice.id, {
                        "contents": this.detailNotice.title,
                        "title": this.detailNotice.contents,
                        "userName": this.user.id
                    },
                    {
                        "Content-Type": "application/json" ,
                        "X-AUTH-KEY": this.user.token,
                        "Access-Control-Allow-Origin": "*"
                    }
                )
                .then(response => {
                    console.log(response);
                    alert("공지사항이 갱신되었습니다.");
                    this.getNotice(this.currentPage);
                })
                .catch(response => {
                    console.log(response);
                    alert("공지사항 갱신에 실패하였습니다.");

                })
                .finally(() => {
                    // always executed
                }
            );
        },

        deleteNotice: function(index) {

            //var targetIndex = (this.currentPageSize - index ) + (interval * (this.currentPage-1));

            //console.log("Delete Target : " + targetIndex + ", " + this.currentPage + ", " + index);

            console.log(">>> Delete Target : " + index);

            axios
                .delete("http://localhost:7000/api/notice/"+index, {
                        params: { userName : this.user.id }
                    },
                    {
                        "Content-Type": "application/json" ,
                        "X-AUTH-KEY": this.user.token,
                        "Access-Control-Allow-Origin": "*"
                    }
                )
                .then(response => {
                    console.log(response);
                    alert("공지사항이 삭제되었습니다.");
                    this.getNotice(this.currentPage);
                })
                .catch(response => {
                    console.log(response);
                    alert("공지사항 삭제에 실패하였습니다.");

                })
                .finally(() => {
                    // always executed
                }
            );
        },

        registerNotice: function() {
            if (this.detailNotice.title.length == 0 ) {
                alert("제목을 입력해 주십시오.");
                return;
            }

            if (this.detailNotice.contents.length == 0 ) {
                alert("본문을 입력해 주십시오.");
                return;
            }

            if (this.detailNotice.id.length == 0) this.insertNotice();
            else this.updateNotice();
        },

		retrieveNotice: function(index) {

            var targetIndex = index;
            this.detailNotice = this.notices[targetIndex];
        },
        
        login: function(){
            console.log('>>> loginStatus : ' + this.loginStatus);

            if (this.isLogin){
                this.user ={ id: '', password: '', token: '', role: ''};
                this.isLogin = false;
                this.loginStatus = "Login";
                return;
            }
          
            axios
                .get("http://localhost:7000/login", {
                    params: {
                        id: this.user.id,
                        password: this.user.password,
                    }
                })
                .then(response => {
                    console.log(response);
                    this.user.token = response.data;
                    this.user.password = '';
                    this.isLogin = true;
                    this.loginStatus = "Log Out";   
                    this.newNotice();
                })
                .catch(response => {
                    alert("로그인에 실패하였습니다.");

                })
                .finally(() => {
                    // always executed
                }
            );  
        },

        getNotice: function(currentPage){



            if (currentPage > this.totalPages) {
                this.currentPage = currentPage;
                currentPage = this.totalPages;
            }

            let page = currentPage;

            this.notices = [];

            axios
                .get("http://localhost:7000/api/notice", {
                    headers:{
                         "Access-Control-Expose-Headers":"X-TOTAL-COUNT,X-TOTAL-PAGE"
                    },
                    params: {
                        page: page-1,
                        size: interval,
                        sort: 'id,DESC',
                    }
                })
                .then(response => {
                       response.data.forEach(notice => {
                        this.notices.push(notice);
                    });

                    this.totalPages = parseInt(response.headers["x-total-page"]);
                    this.totalCount = parseInt(response.headers["x-total-count"]);
                    this.currentPageSize = parseInt(response.headers["x-current-page-size"]);

                })
                .catch(response => {
                    //alert("조회에 실패했습니다." + response);

                })
                .finally(() => {

                }
            );  
        },
    },
    created() {
        this.getNotice(this.currentPage);
        //this.initEvents();
    },
    mounted() {
        console.log('mounted');
        //this.fetchEvents();

        //console.log('>>> Notice size : ' + this.events.length);
        //this.totalPages = parseInt(this.totalCount / interval) + ((this.totalCount % interval) ? 1 : 0);
    },
    watch: {
        currentPage: function(newCurrentPage) {
            console.log('>>> Check Current Page : ' + newCurrentPage);
            this.getNotice(newCurrentPage);
        }
    },
};
</script>

<style scoped>
table {
    width: 100%;
    border-top: 1px solid #ccc;
    border-collapse: collapse;
}
th {
    background-color: #f5f5f5;
    border-bottom: 1px solid #ccc;
    padding: 10px;
}
td {
    border-bottom: 1px solid #ccc;
    padding: 10px;
}
td.table-hover:hover {

    color: #555;
    text-decoration: none;
    background-color: #f5f5f5;

}
.navbar-default {

    background-color: #f8f8f8;
    border-color: #e7e7e7;

}
.navbar {

    border-radius: 4px;

}
.navbar {

    position: relative;
    min-height: 50px;
    margin-bottom: 20px;
    border: 1px solid transparent;
        border-top-color: transparent;
        border-right-color: transparent;
        border-bottom-color: transparent;
        border-left-color: transparent;

}

.panel-heading {

    padding: 10px 15px;
    border-bottom: 1px solid transparent;
    border-bottom-color: transparent;
    border-top-left-radius: 3px;
    border-top-right-radius: 3px;

}
.panel-body {

    padding: 15px;

}
.panel-heading {

    padding: 10px 15px;
    border-bottom: 1px solid transparent;
        border-bottom-color: transparent;
    border-top-left-radius: 3px;
    border-top-right-radius: 3px;


}
.panel-default > .panel-heading {

    color: #333;
    background-color: #f5f5f5;
    border-color: #ddd;

}
.btn-primary:hover {

    color: #fff;
    background-color: #286090;
    border-color: #204d74;

}
.btn.focus, .btn:focus, .btn:hover {

    color: #333;
    text-decoration: none;

}
.btn-primary {

    color: #fff;
    background-color: #337ab7;
    border-color: #2e6da4;

}
.btn {

    display: inline-block;
    margin-bottom: 0;
    font-weight: 400;
    text-align: center;
    white-space: nowrap;
    vertical-align: middle;
    -ms-touch-action: manipulation;
    touch-action: manipulation;
    cursor: pointer;
    background-image: none;
    border: 1px solid transparent;
        border-top-color: transparent;
        border-right-color: transparent;
        border-bottom-color: transparent;
        border-left-color: transparent;
    padding: 6px 12px;
    font-size: 14px;
    line-height: 1.42857143;
    border-radius: 4px;
    -webkit-user-select: none;
    -moz-user-select: none;
    -ms-user-select: none;
    user-select: none;

}

.container {

    width: 1170px;

}
.form-control {

    margin-bottom: 10px;

}
.form-control {

    display: block;
    width: 100%;
    height: 34px;
    padding: 6px 12px;
    font-size: 14px;
    line-height: 1.42857143;
    color: #555;
    background-color: #fff;
    background-image: none;
    border: 1px solid #ccc;
    border-radius: 4px;
    -webkit-box-shadow: inset 0 1px 1px rgba(0,0,0,.075);
    box-shadow: inset 0 1px 1px rgba(0,0,0,.075);
    -webkit-transition: border-color ease-in-out .15s,box-shadow ease-in-out .15s;
    -o-transition: border-color ease-in-out .15s,box-shadow ease-in-out .15s;
    -webkit-transition: border-color ease-in-out .15s,-webkit-box-shadow ease-in-out .15s;
    transition: border-color ease-in-out .15s,-webkit-box-shadow ease-in-out .15s;
    transition: border-color ease-in-out .15s,box-shadow ease-in-out .15s;
    transition: border-color ease-in-out .15s,box-shadow ease-in-out .15s,-webkit-box-shadow ease-in-out .15s;

}
.col-sm-7 {

    width: 58.33333333%;

}
.col-sm-5 {

    width: 41.66666667%;

}
.col-sm-1, .col-sm-10, .col-sm-11, .col-sm-12, .col-sm-2, .col-sm-3, .col-sm-4, .col-sm-5, .col-sm-6, .col-sm-7, .col-sm-8, .col-sm-9 {

    float: left;

}
.col-lg-1, .col-lg-10, .col-lg-11, .col-lg-12, .col-lg-2, .col-lg-3, .col-lg-4, .col-lg-5, .col-lg-6, .col-lg-7, .col-lg-8, .col-lg-9, .col-md-1, .col-md-10, .col-md-11, .col-md-12, .col-md-2, .col-md-3, .col-md-4, .col-md-5, .col-md-6, .col-md-7, .col-md-8, .col-md-9, .col-sm-1, .col-sm-10, .col-sm-11, .col-sm-12, .col-sm-2, .col-sm-3, .col-sm-4, .col-sm-5, .col-sm-6, .col-sm-7, .col-sm-8, .col-sm-9, .col-xs-1, .col-xs-10, .col-xs-11, .col-xs-12, .col-xs-2, .col-xs-3, .col-xs-4, .col-xs-5, .col-xs-6, .col-xs-7, .col-xs-8, .col-xs-9 {

    position: relative;
    min-height: 1px;
    padding-right: 15px;
    padding-left: 15px;

}
.navbar-default .navbar-brand:focus, .navbar-default .navbar-brand:hover {

    color: #5e5e5e;
    background-color: transparent;

}
.navbar > .container .navbar-brand, .navbar > .container-fluid .navbar-brand {

    margin-left: -15px;

}
.navbar-default .navbar-brand {

    color: #777;

}
.navbar-brand:focus, .navbar-brand:hover {

    text-decoration: none;

}
a:focus, a:hover {

    color: #23527c;
    text-decoration: underline;

}
a:active, a:hover {

    outline: 0;

}
.navbar-brand {

    float: left;
    height: 50px;
    padding: 15px 15px;
    font-size: 18px;
    line-height: 20px;

}
.glyphicon {

    position: relative;
    top: 1px;
    display: inline-block;
    font-family: "Glyphicons Halflings";
    font-style: normal;
    font-weight: 400;
    line-height: 1;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;

}
.container-fluid {

    padding-right: 15px;
    padding-left: 15px;
    margin-right: auto;
    margin-left: auto;

}
a.list-group-item:focus, a.list-group-item:hover, button.list-group-item:focus, button.list-group-item:hover {

    color: #555;
    text-decoration: none;
    background-color: #f5f5f5;

}
.list-group-item:first-child {

    border-top-left-radius: 4px;
    border-top-right-radius: 4px;

}
a.list-group-item, button.list-group-item {

    color: #555;

}
a:focus, a:hover {

    color: #23527c;
    text-decoration: underline;

}
a:active, a:hover {

    outline: 0;

}
.list-group-item {

    position: relative;
    display: block;
    padding: 10px 15px;
    margin-bottom: -1px;
    background-color: #fff;
    border: 1px solid #ddd;

}
.h4, h4 {

    font-size: 18px;

}
.h4, .h5, .h6, h4, h5, h6 {

    margin-top: 10px;
    margin-bottom: 10px;

}
.h1, .h2, .h3, .h4, .h5, .h6, h1, h2, h3, h4, h5, h6 {

    font-family: inherit;
    font-weight: 500;
    line-height: 1.1;
    color: inherit;

}
.panel-default {
    border: solid 1px #ccc;

}
.panel {

    margin-bottom: 20px;
    background-color: #fff;
    border-radius: 4px;
    -webkit-box-shadow: 0 1px 1px rgba(0,0,0,.05);
    box-shadow: 0 1px 1px rgba(0,0,0,.05);

}
a {

    color: #337ab7;
    text-decoration: none;

}
a {

    background-color: transparent;

}
* {

    -webkit-box-sizing: border-box;
    -moz-box-sizing: border-box;
    box-sizing: border-box;

}
</style>