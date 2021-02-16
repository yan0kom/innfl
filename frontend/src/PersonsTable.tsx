import React from 'react';
import {IPerson} from './BackendServices';
import {IHandlers} from './App';
import './PersonsTable.css';

interface ITableProps {
  persons: IPerson[];
  handlers: IHandlers;
}
class PersonsTable extends React.Component<ITableProps> {
  constructor(props: ITableProps) {
    super(props);
  }

  render(): JSX.Element {
    const rows = this.props.persons.map((p, i) =>
      <TableRow key={p.id} rowIdx={i+1} person={p} handlers={this.props.handlers}/>);
    return (
      <table className="persons-table">
        <thead>
          <tr>
            <th className="persons-table-header-cell">№</th>
            <th className="persons-table-header-cell">ФИО</th>
            <th className="persons-table-header-cell">Дата рождения</th>
            <th className="persons-table-header-cell">ИНН</th><th>Действия</th>
          </tr>
        </thead>
        <tbody>
          {rows}
        </tbody>
      </table>
    );
  }
}

interface ITableRowProps {
  rowIdx: number;
  person: IPerson;
  handlers: IHandlers;
}
class TableRow extends React.Component<ITableRowProps> {
  constructor(props: ITableRowProps) {
    super(props);
  }

  render(): JSX.Element {
    return (
      <tr className="persons-table-row">
        <IndexCell idx = {this.props.rowIdx} />
        <FullNameCell
          firstName = {this.props.person.firstName}
          lastName = {this.props.person.lastName}
          patronymic = {this.props.person.patronymic} />
        <BirhtdayCell dateOfBirth={this.props.person.dateOfBirth} />
        <InnCell innState={this.props.person.innState} inn={this.props.person.inn} personId={this.props.person.id} handlers={this.props.handlers} />
        <ActionsCell handlers={this.props.handlers} personId={this.props.person.id}/>
      </tr>
    );
  }
}

function IndexCell(props: {idx: number}): JSX.Element {
  return <td className="persons-table-cell">{props.idx}</td>;
}

function FullNameCell(props: {firstName: string, lastName: string, patronymic: string}): JSX.Element {
  let fullName = props.lastName + ' ' + props.firstName + (props.patronymic.length > 0 ? ' ' + props.patronymic : '');
  return <td className="persons-table-cell full-name-cell">{fullName}</td>;
}

function BirhtdayCell(props: {dateOfBirth: string}): JSX.Element {
  return <td className="persons-table-cell">{new Date(props.dateOfBirth).toLocaleDateString()}</td>;
}

function InnCell(props: {innState: string, inn: string, personId: number, handlers: IHandlers}): JSX.Element {
  let content: JSX.Element;
  switch (props.innState) {
    case 'Fetched':
      content = <span>{props.inn}</span>;
      break;
    case 'Queued':
      content = <span>В очереди</span>;
      break;
    case 'Queried':
      content = <span>Запрошено</span>;
      break;
    case 'NotFound':
      content = <span>Не найдено</span>;
      break;
    case 'None':
      content = <button onClick={() => props.handlers.queryPersonInn(props.personId)}>Запросить</button>;
      break;
    default:
      content = <span>Unknown state: {props.innState}</span>;
  }
  return <td className="persons-table-cell">{content}</td>;
}

function ActionsCell(props: {handlers: IHandlers, personId: number}): JSX.Element {
  return (
    <td className="persons-table-cell">
      <button onClick={() => props.handlers.editPerson(props.personId)}>Изменить</button>
      <button onClick={() => props.handlers.deletePerson(props.personId)}>Удалить</button>
    </td>
  );
}

export default PersonsTable;
