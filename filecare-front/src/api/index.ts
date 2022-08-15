import {UserDetails} from "@/interfaces";

const serviceURI: string = 'http://' + window.location.hostname + ":8080/"

export default {
    concatURI(endpoint: string): string {
        return serviceURI + endpoint
    },
    async addTokenHeader(headers: Headers | undefined): Promise<Headers | undefined> {
        if (headers === null || headers === undefined) {
            headers = new Headers()
        }
        return await this.obtainToken()
            .then(token => {
                headers?.set("Authorization", `Bearer ${token}`)
                return headers
            })
    },
    fetch(method: string, endpoint: string, body: any, headers: Headers | undefined, needAuth: boolean): Promise<Response> {
    // fetch(method: string, endpoint: string, body: string | undefined, headers: Headers | undefined, needAuth: boolean): Promise<Response> {
      if (needAuth) {
          return this.addTokenHeader(headers)
              .then(headers =>
                    fetch(this.concatURI(endpoint), {
                        method: method,
                        body: body,
                        headers: headers
                    }))
      } else {
          return fetch(this.concatURI(endpoint), {
              method: method,
              body: body,
              headers: headers
          })
      }
    },
    get(endpoint: string, headers: Headers | undefined, needAuth: boolean): Promise<Response> {
        return this.fetch('GET', endpoint, undefined, headers, needAuth)
    },
    post(endpoint: string, body: any, headers: Headers | undefined, needAuth: boolean): Promise<Response> {
        return this.fetch('POST', endpoint, body, headers, needAuth)
    },
    put(endpoint: string, body: string, headers: Headers | undefined, needAuth: boolean): Promise<Response> {
        return this.fetch('PUT', endpoint, body, headers, needAuth)
    },
    delete(endpoint: string, body: any, headers: Headers | undefined, needAuth: boolean): Promise<Response> {
        return this.fetch('DELETE', endpoint, body, headers, needAuth)
    },

    obtainUserDetails(): UserDetails | null {
        const userDetailsString: string | null= localStorage.getItem('filecare-userdetails')
        if (userDetailsString) {
            return JSON.parse(userDetailsString)
        } else {
            return null
        }
    },

    async obtainToken(): Promise<string> {
        const localToken: string | null = localStorage.getItem('filecare-token')

        if (localToken !== null && localToken !== undefined) {
            // TODO добавить проверку годность токена (срок действия)
            return Promise.resolve(localToken)
        } else {
            const userDetails: UserDetails | null = this.obtainUserDetails();
            if (userDetails !== null) {
                // TODO добавить проверку валидности UserDetails
                return await this.post('authentication', JSON.stringify(userDetails), undefined, false)
                    .then(response => response.json())
                    .then(json => json.token);
            } else {
                throw new Error("UserDetails is absent in localstorage.")
            }
        }
    },
    removeUserData() {
        localStorage.removeItem('filecare-token')
        localStorage.removeItem('filecare-userdetails')
    }
}