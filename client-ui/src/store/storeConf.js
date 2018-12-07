import { combineReducers, createStore } from 'redux'
import toggleAuthentication from './reducers/authReducer'
import toggleMenu from './reducers/menuReducer'
import toggleBuyingBox from './reducers/buyingBoxReducer'

const rootReducer = combineReducers({
  authentication: toggleAuthentication,
  menu: toggleMenu,
  buyingBox: toggleBuyingBox,
})

export default createStore(rootReducer)
