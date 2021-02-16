import React from 'react';
import Modal from 'react-modal';
import {Person} from './BackendServices';

import './PersonEditFrame.css';

Modal.setAppElement("#root");

interface PersonEditFrameProps {
  person: Person
  onClose: () => void
  onSave: (p: Person) => void
}
export default class PersonEditFrame extends React.Component<PersonEditFrameProps, {person: Person, fieldErrors: Map<string, boolean>}> {
  constructor(props: PersonEditFrameProps) {
    super(props)
    this.state = {person: {...props.person}, fieldErrors: new Map<string, boolean>()};
    this.state.person.dateOfBirth = new Date(this.state.person.dateOfBirth).toLocaleDateString();

    this.onSave = this.onSave.bind(this);
    this.lastNameChanged = this.lastNameChanged.bind(this);
    this.firstNameChanged = this.firstNameChanged.bind(this);
    this.patronymicChanged = this.patronymicChanged.bind(this);
    this.dateOfBirthChanged = this.dateOfBirthChanged.bind(this);
  }

  private pad(n: number): string {
    if (n < 10) {
      return '0' + n;
    }
    return n.toString();
  }

  onSave() {
    const person = this.state.person;
    let hasError = false;
    const errors = new Map<string, boolean>();

    if (person.lastName.length == 0) {
      errors.set("lastName", true);
    }
    if (person.firstName.length == 0) {
      errors.set("firstName", true);
    }
    const nd = new Date(this.state.person.dateOfBirth);
    if (isNaN(nd.valueOf())) {
      errors.set("dateOfBirth", true);
    }
    this.setState({fieldErrors: errors});

    if (errors.size == 0) {
      person.dateOfBirth = this.pad(nd.getFullYear()) + '-' + this.pad(nd.getMonth()+1) + '-' + this.pad(nd.getDate());
      this.props.onSave(person);
    }
  }

  lastNameChanged(ev: React.ChangeEvent<HTMLInputElement>) {
    this.setState(state => {
      state.person.lastName = ev.target.value;
      return state;
    });
  }

  firstNameChanged(ev: React.ChangeEvent<HTMLInputElement>) {
    this.setState(state => {
      state.person.firstName = ev.target.value;
      return state;
    });
  }

  patronymicChanged(ev: React.ChangeEvent<HTMLInputElement>) {
    this.setState(state => {
      state.person.patronymic = ev.target.value;
      return state;
    });
  }

  dateOfBirthChanged(ev: React.ChangeEvent<HTMLInputElement>) {
    this.setState(state => {
      state.person.dateOfBirth = ev.target.value;
      return state;
    });
  }

  render(): JSX.Element { return (
    <Modal isOpen={true}
        onRequestClose={this.props.onClose}
        className="edit-form"
        overlayClassName="edit-form-overlay"
        closeTimeoutMS={500}>
      <table><tbody>
        <tr>
          <td className={this.state.fieldErrors.get("lastName") ? "edit-form-label-error" : "edit-form-label"}>Фамилия</td>
          <td><input type="text" value={this.state.person.lastName} onChange={this.lastNameChanged}/></td>
        </tr>
        <tr>
          <td className={this.state.fieldErrors.get("firstName") ? "edit-form-label-error" : "edit-form-label"}>Имя</td>
          <td><input type="text" value={this.state.person.firstName} onChange={this.firstNameChanged}/></td>
        </tr>
        <tr>
          <td>Отчество</td>
          <td><input type="text" value={this.state.person.patronymic} onChange={this.patronymicChanged}/></td>
        </tr>
        <tr>
          <td className={this.state.fieldErrors.get("dateOfBirth") ? "edit-form-label-error" : "edit-form-label"}>Дата рождения</td>
          <td><input type="text" value={this.state.person.dateOfBirth} onChange={this.dateOfBirthChanged}/></td>
        </tr>
      </tbody></table>
      <div>
        <button onClick={this.onSave}>Сохранить</button>
        <button onClick={this.props.onClose}>Закрыть</button>
      </div>
    </Modal>
  );}
}
