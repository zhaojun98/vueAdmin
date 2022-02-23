import Vue from 'vue'
import Vuex from 'vuex'
import menus from "./modules/menus";

Vue.use(Vuex)

export default new Vuex.Store({
	state: {
		token: ''

	},
	mutations: {

		SET_TOKEN: (state, token) => {
			state.token = token
			// localStorage.setItem("token", token)
			sessionStorage.setItem("token", token)		//解决token失效的问题
		},


	},
	actions: {},
	modules: {
		menus
	}
})
