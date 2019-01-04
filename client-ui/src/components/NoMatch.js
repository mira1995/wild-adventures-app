import React from 'react'
import { strings } from '../helpers/strings'

const NoMatch = ({ location }) => (
  <div>
    <h2>{strings.routes.notMatch}</h2>
    <code>{location.pathname}</code>
  </div>
)

export default NoMatch
