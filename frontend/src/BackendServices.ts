export interface IPerson {
  readonly id: number;
  firstName: string;
  lastName: string;
  patronymic: string;
  dateOfBirth: string;
  readonly innState: string;
  readonly inn: string;
}

export class Person {
  readonly id: number = -1;
  firstName: string = '';
  lastName: string = '';
  patronymic: string = '';
  dateOfBirth: string = '2000-01-01';
  readonly innState: string = '';
  readonly inn: string = '';
}

const apiRoot = './api/'

class BackendService {
  private endpoint: string;
  private fetched: boolean;
  private error: boolean;
  private errMsg: string;

  constructor(endpoint: string) {
    this.endpoint = apiRoot + endpoint;
    this.fetched = false;
    this.error = false;
    this.errMsg = '';
  }

  fetchData(onSuccess: (data: any) => void, onError: (errmsg: string) => void) {
    this.fetched = false;
    this.error = false;
    this.errMsg = '';

    return window.fetch(this.endpoint)
      .then(response => {
        if (!response.ok) {
          throw new Error('Ошибка получения данных: ' + response.status + ' - ' + response.statusText);
        }
        return response.json()
      })
      .then(
        result => {
          this.fetched = true
          onSuccess(result)
        },
        (err) => {
          this.error = true
          this.errMsg = err.message;
          onError(this.errMsg)
        }
      )
      .catch(err => {
        this.error = true
        this.errMsg = err.message;
        onError(this.errMsg)
    });
  }

  async callBackend(url: string, method: string, data: any | null): Promise<any> {
    // Default options are marked with *
    const response = await fetch(url, {
      method: method, // *GET, POST, PUT, DELETE, etc.
      mode: 'cors', // no-cors, *cors, same-origin
      cache: 'no-cache', // *default, no-cache, reload, force-cache, only-if-cached
      credentials: 'same-origin', // include, *same-origin, omit
      headers: {
        'Content-Type': 'application/json'
        // 'Content-Type': 'application/x-www-form-urlencoded',
      },
      redirect: 'follow', // manual, *follow, error
      referrerPolicy: 'no-referrer', // no-referrer, *no-referrer-when-downgrade, origin, origin-when-cross-origin, same-origin, strict-origin, strict-origin-when-cross-origin, unsafe-url
      body: JSON.stringify(data) // body data type must match "Content-Type" header
    });
    return response.json(); // parses JSON response into native JavaScript objects
  }

  postData(url: string, data: any): Promise<any> {
    return this.callBackend(url, 'POST', data);
  }

  putData(url: string, data: any): Promise<any> {
    return this.callBackend(url, 'PUT', data);
  }

  deleteData(url: string): Promise<any> {
    return this.callBackend(url, 'DELETE', null);
  }

  isFetched() {
    return this.fetched;
  }

  hasError() {
    return this.error;
  }

  getErrMsg() {
    return this.errMsg;
  }
}

export class PersonsService extends BackendService {
  constructor() {
    super('persons');
  }

  async getPersons(): Promise<IPerson[]> {
    const getPromise = new Promise<IPerson[]>((resolve, reject) =>
      this.fetchData(
        (data) => resolve(data),
        errMsg => reject(errMsg)
      )
    );
    return getPromise;
  }

  async createPerson(person: Person): Promise<Person> {
    return this.postData(apiRoot+'person', person);
  }

  async updatePerson(person: Person): Promise<Person> {
    return this.putData(apiRoot+'person/'+person.id, person);
  }

  async deletePerson(personId: number): Promise<string> {
    return this.deleteData(apiRoot+'person/'+personId);
  }

  async queryPersonInn(personId: number): Promise<string> {
    return this.postData(apiRoot+'person/'+personId+'/queryInn', null);
  }
}
