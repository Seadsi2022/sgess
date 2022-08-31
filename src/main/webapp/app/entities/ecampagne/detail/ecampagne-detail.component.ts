import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEcampagne } from '../ecampagne.model';

@Component({
  selector: 'jhi-ecampagne-detail',
  templateUrl: './ecampagne-detail.component.html',
})
export class EcampagneDetailComponent implements OnInit {
  ecampagne: IEcampagne | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ecampagne }) => {
      this.ecampagne = ecampagne;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
