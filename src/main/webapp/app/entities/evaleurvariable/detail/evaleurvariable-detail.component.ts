import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEvaleurvariable } from '../evaleurvariable.model';

@Component({
  selector: 'jhi-evaleurvariable-detail',
  templateUrl: './evaleurvariable-detail.component.html',
})
export class EvaleurvariableDetailComponent implements OnInit {
  evaleurvariable: IEvaleurvariable | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ evaleurvariable }) => {
      this.evaleurvariable = evaleurvariable;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
