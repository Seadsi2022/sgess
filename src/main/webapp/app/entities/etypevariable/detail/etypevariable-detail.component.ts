import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEtypevariable } from '../etypevariable.model';

@Component({
  selector: 'jhi-etypevariable-detail',
  templateUrl: './etypevariable-detail.component.html',
})
export class EtypevariableDetailComponent implements OnInit {
  etypevariable: IEtypevariable | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ etypevariable }) => {
      this.etypevariable = etypevariable;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
