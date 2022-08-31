import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEgroupevariable } from '../egroupevariable.model';

@Component({
  selector: 'jhi-egroupevariable-detail',
  templateUrl: './egroupevariable-detail.component.html',
})
export class EgroupevariableDetailComponent implements OnInit {
  egroupevariable: IEgroupevariable | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ egroupevariable }) => {
      this.egroupevariable = egroupevariable;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
