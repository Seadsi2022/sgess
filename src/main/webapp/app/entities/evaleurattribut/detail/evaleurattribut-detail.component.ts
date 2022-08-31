import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEvaleurattribut } from '../evaleurattribut.model';

@Component({
  selector: 'jhi-evaleurattribut-detail',
  templateUrl: './evaleurattribut-detail.component.html',
})
export class EvaleurattributDetailComponent implements OnInit {
  evaleurattribut: IEvaleurattribut | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ evaleurattribut }) => {
      this.evaleurattribut = evaleurattribut;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
