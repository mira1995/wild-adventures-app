import axios from 'axios'
import { API } from '../helpers/constants'

export const http = axios.create({
  baseURL: API.BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
})
