import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEeventualitevariable } from '../eeventualitevariable.model';

@Component({
  selector: 'jhi-eeventualitevariable-detail',
  templateUrl: './eeventualitevariable-detail.component.html',
})
export class EeventualitevariableDetailComponent implements OnInit {
  eeventualitevariable: IEeventualitevariable | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ eeventualitevariable }) => {
      this.eeventualitevariable = eeventualitevariable;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
