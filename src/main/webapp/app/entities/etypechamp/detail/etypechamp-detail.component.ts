import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEtypechamp } from '../etypechamp.model';

@Component({
  selector: 'jhi-etypechamp-detail',
  templateUrl: './etypechamp-detail.component.html',
})
export class EtypechampDetailComponent implements OnInit {
  etypechamp: IEtypechamp | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ etypechamp }) => {
      this.etypechamp = etypechamp;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
