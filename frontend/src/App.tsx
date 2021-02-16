import React from 'react';

import {IPerson, Person, PersonsService} from './BackendServices';
import PersonsTable from './PersonsTable';
import PersonEditFrame from './PersonEditFrame';

import './App.css';

function App() {
  return (
    <div className="App">
      <PersonsTableFrame />
    </div>
  );
}

export default App;

export interface IHandlers {
  editPerson: (id: number) => void;
  deletePerson: (id: number) => void;
  queryPersonInn: (id: number) => void;
}

class PersonsTableFrame extends React.Component<{}, {persons: IPerson[], showEditForm: boolean}> {
  private personsService = new PersonsService();
  private handlers: IHandlers;
  private changingPerson = new Person();

  constructor(props: {}) {
    super(props)

    this.handlers = {
      editPerson: this.editPerson.bind(this),
      deletePerson: this.deletePerson.bind(this),
      queryPersonInn: this.queryPersonInn.bind(this)
    }

    this.closeEditForm = this.closeEditForm.bind(this);
    this.savePerson = this.savePerson.bind(this);
    this.addPerson = this.addPerson.bind(this);
  }

  componentDidMount() {
    this.refreshTable();
  }

  refreshTable() {
    this.personsService.getPersons().then(personsList => {
      this.setState({persons: personsList});
    }).catch(() => {
      this.setState({persons: []});
    });
  }

  editPerson(id: number) {
    const changingPersonIdx = this.state.persons.findIndex(p => p.id == id);
    if (changingPersonIdx != -1) {
      this.changingPerson = this.state.persons[changingPersonIdx];
      this.setState({showEditForm: true});
    }
  }

  closeEditForm() {
    this.setState({showEditForm: false});
  }

  savePerson(person: Person) {
    if (person.id == -1) {
      this.personsService.createPerson(person).then(() => this.refreshTable());
    } else {
      this.personsService.updatePerson(person).then(updatedPerson => {
        this.setState(state => {
          return {persons: state.persons.map((p) => (p.id == updatedPerson.id ? updatedPerson : p))}
        });
      });
    }
    this.closeEditForm();
  }

  addPerson() {
    this.changingPerson = new Person();
    this.setState({showEditForm: true});
  }

  deletePerson(id: number) {
    this.personsService.deletePerson(id).then(() => this.refreshTable());
  }

  queryPersonInn(id: number) {
    this.personsService.queryPersonInn(id).then(() => this.refreshTable());
  }

  render(): JSX.Element {
    if (this.personsService.isFetched()) {
      const header = <div className="persons-frame-header">Физические лица</div>
      const table = <PersonsTable persons={this.state.persons} handlers={this.handlers}/>
      const footer = <div className="persons-frame-footer"><button onClick={this.addPerson}>Добавить</button></div>
      const editForm = this.state.showEditForm ?
            <PersonEditFrame person={this.changingPerson} onClose={this.closeEditForm} onSave={this.savePerson} /> :
            <div></div>
      return (
        <div>
          {header}
          {table}
          {footer}
          {editForm}
        </div>
      );
    }
    if (this.personsService.hasError()) {
      return <p className="error">{this.personsService.getErrMsg()}</p>
    }
    return <p>Получение данных...</p>
  }
}
