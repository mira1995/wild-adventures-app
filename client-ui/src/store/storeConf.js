import { combineReducers, createStore } from 'redux'
import toggleAuthentication from './reducers/authReducer'
import toggleMenu from './reducers/menuReducer'
import toggleBuyingBox from './reducers/buyingBoxReducer'
import toggleLanguage from './reducers/languageReducer'

const rootReducer = combineReducers({
  authentication: toggleAuthentication,
  menu: toggleMenu,
  buyingBox: toggleBuyingBox,
  language: toggleLanguage,
})

export default createStore(rootReducer)
