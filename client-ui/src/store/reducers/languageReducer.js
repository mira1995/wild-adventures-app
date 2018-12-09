import { TOGGLE_LANGUAGE } from '../actions/types'

const initialState = { code: null }

function toggleLanguage(state = initialState, action) {
  let nextState
  switch (action.type) {
    case TOGGLE_LANGUAGE:
      nextState = {
        ...state,
        code: action.value,
      }
      return nextState || state
    default:
      return state
  }
}

export default toggleLanguage
